package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.ejbs;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.persistence.*;
import jakarta.validation.ConstraintViolationException;
import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.*;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.Order;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.enums.OrderStatus;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.*;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Stateless
public class OrderBean {

    @PersistenceContext
    private EntityManager entityManager;

    @EJB
    private QueryBean<Order> orderQueryBean;

    @EJB
    private OrderLogBean orderLogBean;

    public List<Order> getOrders(Map<String, String> filterMap, int pageNumber, int pageSize)
            throws IllegalArgumentException {
        Map<String, String> orderMap = new LinkedHashMap<>();
        orderMap.put("date", "desc");
        return orderQueryBean.getEntities(Order.class, filterMap, orderMap, pageNumber, pageSize);
    }

    public long getOrdersCount(Map<String, String> filterMap) {
        return orderQueryBean.getEntitiesCount(Order.class, filterMap);
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public long create(String address, String phoneNumber, String postCode, String city, double totalPrice, Date date, String username, List<OrderItem> orderItems)
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
            var order = new Order(address, phoneNumber, postCode, city, totalPrice, date, OrderStatus.PENDING, customer, orderItems);
            entityManager.persist(order);

            for (var orderItem : order.getOrderItems()){
                orderItem.setOrder(order);
                orderItem.setQuantityLeft(orderItem.getQuantity());
                entityManager.persist(orderItem);
            }
            var orderLog = orderLogBean.create("Order created", order.getId(), OrderStatus.PENDING.toString(), null);


            return order.getId();
        } catch (ConstraintViolationException err) {
            throw new MyConstraintViolationException(err);
        }
    }

    public Order find(Long id) throws MyEntityNotFoundException {
        var order = entityManager.find(Order.class, id);
        if (order == null)
        {
            throw new MyEntityNotFoundException("Order with id #" + id + " was not found.");
        }

        Hibernate.initialize(order.getOrderItems());

        return order;
    }

    public Order findWithDeliveries(Long id) throws MyEntityNotFoundException {
        var order = find(id);

        Hibernate.initialize(order.getDeliveries());

        for (var delivery : order.getDeliveries()) {
            Hibernate.initialize(delivery.getPackages());
            for (var aPackage : delivery.getPackages()){
                Hibernate.initialize(aPackage.getProducts());
            }
        }

        return order;
    }

    public void updateStatus(
            long id,
            OrderStatus orderStatus
    ) throws MyEntityNotFoundException, MyIllegalConstraintException, MyConstraintViolationException {
        var order  = this.find(id);

        if (orderStatus.ordinal() <= order.getStatus().ordinal()){
            throw new MyIllegalConstraintException("Cannot update status to a previous or current value. Current Value: " + orderStatus.toString());
        }

        if (order.getStatus() == OrderStatus.REJECTED){
            throw new MyIllegalConstraintException("Cannot update status of a Rejected Order");
        }


        entityManager.lock(order, LockModeType.OPTIMISTIC);

        order.setStatus(orderStatus);
        var orderLog = orderLogBean.create("Order status updated to " + orderStatus.toString(), order.getId(), orderStatus.toString(), null);

    }
}
