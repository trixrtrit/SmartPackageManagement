package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.ejbs;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.ConstraintViolationException;
import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.*;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.Package;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.enums.DeliveryStatus;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Stateless
public class DeliveryBean {

    @PersistenceContext
    private EntityManager entityManager;

    @EJB
    private QueryBean<Delivery> deliveryQueryBean;
    @EJB
    private OrderLogBean orderLogBean;

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public long create(Long orderId, ArrayList<Long> packageCodes)
            throws MyEntityExistsException, MyConstraintViolationException, MyEntityNotFoundException, MyValidationException, MyIllegalConstraintException {

        var order = (Order)entityManager.find(Order.class, orderId);

        if (order == null) {
            throw new MyEntityNotFoundException("The order with the id: " + orderId + " does not exist");
        }

        if (packageCodes.stream().count() <= 0){
            throw new MyValidationException("Cannot create delivery without associated packages");
        }

        var orderItems = order.getOrderItems().stream().filter(x -> {
            return x.getQuantityLeft() > 0;
        });

        try {
            var delivery = new Delivery(Instant.now(), DeliveryStatus.DISPATCHED, order);

            for (var packageCode : packageCodes){
                var aPackage = entityManager.createNamedQuery("findActivePackage", Package.class)
                        .setParameter("code", packageCode)
                        .setMaxResults(1)
                        .getResultList()
                        .stream()
                        .findFirst()
                        .orElse(null);

                if (aPackage == null) {
                    throw new MyEntityNotFoundException("The package with the code: " + packageCode + " does not exist");
                }

                //todo adicionar isto quando houver transport packages
                //if (aPackage instance of StandardPackage)
                //{

                var product = aPackage.getProducts().stream().findFirst().get();

                var foundOrderItem = orderItems.filter(orderItem -> {
                    return  aPackage.getPackageType() == orderItem.getPackageType() &&
                            product.getId() == orderItem.getProduct().getId();
                }).findFirst().get();

                if (foundOrderItem == null)
                {
                    throw new MyIllegalConstraintException("Package requested is not part of this order");
                }

                entityManager.lock(foundOrderItem, LockModeType.OPTIMISTIC);
                foundOrderItem.setQuantityLeft(foundOrderItem.getQuantityLeft() - 1);

                //order item has been completed
                if (foundOrderItem.getQuantityLeft() == 0){
                    orderItems = orderItems.filter(x -> x.getQuantityLeft() > 0);
                }

                entityManager.lock(product, LockModeType.OPTIMISTIC);
                switch (aPackage.getPackageType()){
                    case PRIMARY:
                        product.setUnitStock(product.getUnitStock() - 1);
                        break;
                    case SECONDARY:
                        product.setBoxStock(product.getBoxStock() - 1);
                        break;
                    case TERTIARY:
                        product.setContainerStock(product.getContainerStock() - 1);
                        break;
                    default:
                        throw new MyIllegalConstraintException("PackageType not found");
                }

                //}

                delivery.addPackage(aPackage);
                entityManager.lock(aPackage, LockModeType.OPTIMISTIC);
                aPackage.setActive(false);
            }

            delivery.setOrder(order);
            entityManager.persist(delivery);

            return order.getId();
        } catch (ConstraintViolationException err) {
            throw new MyConstraintViolationException(err);
        } catch (MyIllegalConstraintException e) {
            throw new MyIllegalConstraintException(e.getMessage());
        }
    }

    public List<Delivery> getDeliveries(Map<String, String> filterMap, int pageNumber, int pageSize)
            throws IllegalArgumentException {
        Map<String, String> orderMap = new LinkedHashMap<>();
        return deliveryQueryBean.getEntities(Delivery.class, filterMap, orderMap, pageNumber, pageSize);
    }

    public long getDeliveriesCount(Map<String, String> filterMap) {
        return deliveryQueryBean.getEntitiesCount(Delivery.class, filterMap);
    }

    public Delivery find(Long id) throws MyEntityNotFoundException {
        var delivery = entityManager.find(Delivery.class, id);
        if (delivery == null)
        {
            throw new MyEntityNotFoundException("Delivery with id #" + id + " was not found.");
        }

        return delivery;
    }

    public Delivery findWithPackages(Long id) throws MyEntityNotFoundException {
        var delivery = find(id);
        Hibernate.initialize(delivery.getPackages());

        for (var aPackage : delivery.getPackages())
            Hibernate.initialize(aPackage.getProducts());

        return delivery;
    }

    public void updateStatus(Long id, DeliveryStatus deliveryStatus) throws MyEntityNotFoundException, MyIllegalConstraintException, MyConstraintViolationException {
        var delivery = find(id);

        if (delivery.getStatus() == DeliveryStatus.DELIVERED){
            throw new MyIllegalConstraintException("Delivery has been delivered. Cannot change status");
        }

        if (deliveryStatus.ordinal() <= delivery.getStatus().ordinal()){
            throw new MyIllegalConstraintException("Cannot update status to a previous or current value. Current Value: " + delivery.getStatus().toString());
        }
        entityManager.lock(delivery, LockModeType.OPTIMISTIC);
        delivery.setStatus(deliveryStatus);
        var orderLog = orderLogBean.create("Delivery status updated to " + deliveryStatus.toString(), delivery.getOrder().getId(), deliveryStatus.toString(), null);
    }
}
