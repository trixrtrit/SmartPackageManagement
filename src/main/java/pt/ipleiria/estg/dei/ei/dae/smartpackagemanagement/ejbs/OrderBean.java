package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.ejbs;

import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.persistence.*;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;
import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.*;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.Package;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.enums.OrderStatus;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyValidationException;

import java.util.Date;
import java.util.List;

@Stateless
public class OrderBean {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Order> getOrders() {
        Query query = entityManager.createNamedQuery("getOrders", Order.class);
        return query.getResultList();
    }

    public List<Order> getCustomerOrders(String username) {
        Query query = entityManager.createNamedQuery("getCustomerOrders", Order.class);
        query.setParameter("username", username);
        return query.getResultList();
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public long create(String address, double totalPrice, Date date, String username, List<OrderItem> orderItems)
            throws MyEntityExistsException, MyConstraintViolationException, MyEntityNotFoundException, MyValidationException {


        if (totalPrice <= 0){
            throw new MyValidationException("Total price has to be greater than 0€");
        }

        if (orderItems.stream().count() <= 0){
            throw new MyValidationException("Cannot create order without order items");
        }

        double calculatedTotalPrice = 0;

        for (var orderItem : orderItems){
            var product = (Product)entityManager.find(Product.class, orderItem.getProduct().getId());
            if (product == null) {
                throw new MyEntityNotFoundException("The product with the id: " + orderItem.getProduct().getId() + " does not exist");
            }
            orderItem.setProduct(product);

            calculatedTotalPrice += (double) Math.round((orderItem.getPrice() * orderItem.getQuantity()) * 100) / 100.0; //???
        }

        if (totalPrice != calculatedTotalPrice){
            throw new MyValidationException("Calculated price (" + calculatedTotalPrice + "€) does not match total price (" + totalPrice + "€)");
        }

        var customer = (Customer) entityManager.find(Customer.class, username);

        try {
            var order = new Order(address, totalPrice, date, OrderStatus.Pending, customer, orderItems);
            entityManager.persist(order);

            for (var orderItem : order.getOrderItems()){
                orderItem.setOrder(order);
                entityManager.persist(orderItem);
            }

            return order.getId();
        } catch (ConstraintViolationException err) {
            throw new MyConstraintViolationException(err);
        }
    }

    public Order find(Long id) throws MyEntityNotFoundException {
        var  order = entityManager.find(Order.class, id);
        if (order == null)
        {
            throw new MyEntityNotFoundException("Order with id #" + id + " was not found.");
        }
        Hibernate.initialize(order.getOrderItems());

        for (var orderItem : order.getOrderItems()){
            Hibernate.initialize(orderItem.getProduct());
        }
        return order;
    }

    public void updateStatus(
            long id,
            String status
    ) throws MyEntityNotFoundException, MyValidationException {
        var order  = this.find(id);
        var orderStatus = OrderStatus.Pending;
        try{
            orderStatus = OrderStatus.valueOf(status);
        }
        catch (IllegalArgumentException exception){
            throw new MyValidationException("Incorrect value for OrderStatus enum. Received:'" + status + "'");
        }

        var currentStatus = order.getStatus();

        if (currentStatus == OrderStatus.Rejected){
            throw new MyValidationException("Cannot update status of a Rejected Order");
        }

        if (orderStatus.ordinal() <= currentStatus.ordinal()){
            throw new MyValidationException("Cannot assign a new status that is less or equal than the current one");
        }

        //todo add order log after status changes
        entityManager.lock(order, LockModeType.OPTIMISTIC);

        order.setStatus(orderStatus);
    }
}