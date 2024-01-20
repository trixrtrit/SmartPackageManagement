package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.ejbs;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.validation.ConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.*;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.Package;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.enums.DeliveryStatus;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyConstraintViolationException;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Stateless
public class NotificationBean {
    @PersistenceContext
    private EntityManager entityManager;
    @EJB
    private QueryBean<Notification> notificationQueryBean;
    @EJB
    private EmailBean emailBean;

    public List<Notification> getNotifications(Map<String, String> filterMap, int pageNumber, int pageSize)
            throws IllegalArgumentException {
        Map<String, String> orderMap = new LinkedHashMap<>();
        orderMap.put("timestamp", "desc");
        return notificationQueryBean.getEntities(Notification.class, filterMap, orderMap, pageNumber, pageSize);
    }

    public long getNotificationsCount(Map<String, String> filterMap) {
        return notificationQueryBean.getEntitiesCount(Notification.class, filterMap);
    }

    public void fireEnvironmentalNotification(
            String measurement,
            Measurement measurementLine,
            Product product,
            ProductParameter productParameter,
            long packageCode
    ) throws MyConstraintViolationException {
        measurementLine = entityManager.find(Measurement.class, measurementLine.getId());
        product = entityManager.find(Product.class, product.getId());
        productParameter = entityManager.find(ProductParameter.class, productParameter.getId());
        System.out.println("PP "+ productParameter.getId());
        System.out.println("product "+ product.getId());
        System.out.println("measurementLine "+ measurementLine.getId());
        String subject = buildEnvironmentalSubject(product);;
        String text= buildEnvironmentalText(product, productParameter, measurement, packageCode, measurementLine);
        System.out.println("hit1");
        emailBean.send(product.getManufacturer().getEmail(), subject, text);
        try {
            Notification notification = new Notification(text, product.getManufacturer(), measurementLine);
            System.out.println("hit2");
            entityManager.persist(notification);
            System.out.println("hit3");
            sendNotificationToCustomer(packageCode, subject, text, measurementLine);
            System.out.println("hit4");
            sendNotificationToLogisticsOperator(packageCode, subject, text, measurementLine);
            System.out.println("hit5");
        } catch (ConstraintViolationException err) {
            throw new MyConstraintViolationException(err);
        }
    }

    public void fireSecurityNotification(
            String measurement,
            Measurement measurementLine,
            long packageCode
    ) throws MyConstraintViolationException {
        String subject = buildSecuritySubject(packageCode);
        String text = buildSecurityText(measurement, packageCode, measurementLine);
        try {
            sendNotificationToCustomer(packageCode, subject, text, measurementLine);
            sendNotificationToLogisticsOperator(packageCode, subject, text, measurementLine);
        } catch (ConstraintViolationException err) {
            throw new MyConstraintViolationException(err);
        }
    }

    public void fireDeliveryNotification(
            Order order,
            String logisticsUsername
    ) throws MyConstraintViolationException {
        String subject = buildDeliverySubject(order.getId());
        String text = buildDeliveryText(order);
        try {
            LogisticsOperator logisticsOperator = entityManager.find(LogisticsOperator.class, logisticsUsername);
            sendEmailAndPersistNotification(order.getCustomer(), subject, text, null);
            sendEmailAndPersistNotification(logisticsOperator, subject, text, null);
        } catch (ConstraintViolationException err) {
            throw new MyConstraintViolationException(err);
        }
    }

    private void sendNotificationToCustomer(
            long packageCode,
            String subject,
            String text,
            Measurement measurementLine
    ) {
        Query customerQuery = createPackageOwnershipQuery(
                "findCustomerPackage", packageCode, DeliveryStatus.DISPATCHED
        );
        Customer customer = getSingleResultOrNull(customerQuery);
        sendEmailAndPersistNotification(customer, subject, text, measurementLine);
    }

    private void sendNotificationToLogisticsOperator(
            long packageCode,
            String subject,
            String text,
            Measurement measurementLine
    ) {
        Query logisticsOperatorQuery = createPackageOwnershipQuery(
                "findLogisticsOperatorPackage", packageCode, DeliveryStatus.DISPATCHED
        );
        String logisticsOperatorUsername = getSingleResultOrNull(logisticsOperatorQuery);
        if(logisticsOperatorUsername != null) {
            LogisticsOperator logisticsOperator = entityManager.find(LogisticsOperator.class, logisticsOperatorUsername);
            sendEmailAndPersistNotification(logisticsOperator, subject, text, measurementLine);
        }
    }

    private Query createPackageOwnershipQuery(String queryName, long packageCode, DeliveryStatus status) {
        return entityManager.createNamedQuery(queryName, Package.class)
                .setParameter("code", packageCode)
                .setParameter("status", status);
    }

    private <T> T getSingleResultOrNull(Query query) {
        List<T> results = query.getResultList();
        return results.isEmpty() ? null : results.get(0);
    }

    private void sendEmailAndPersistNotification(User recipient, String subject, String text, Measurement measurementLine) {
        if (recipient != null && recipient.getEmail() != null) {
            emailBean.send(recipient.getEmail(), subject, text);
            Notification notification = new Notification(text, recipient, measurementLine);
            entityManager.persist(notification);
        }
    }

    private String buildEnvironmentalSubject(Product product) {
        return "Quality Control | Environmental Warning on product: " + product.getProductReference();
    }

    private String buildEnvironmentalText(
            Product product,
            ProductParameter productParameter,
            String measurement,
            long packageCode,
            Measurement measurementLine
    ) {
        return "Your product " + product.getProductReference() + " has gone beyond its regulated bounds [" +
                productParameter.getMinValue() + "," + productParameter.getMaxValue() + "] \n" +
                "Received measurement " + measurement + " on package code: " + packageCode +
                "On: " + measurementLine.getTimestamp().toString();
    }

    private String buildSecuritySubject(long packageCode) {
        return "Quality Control | Security Warning on package: " + packageCode;
    }

    private String buildSecurityText(
            String measurement,
            long packageCode,
            Measurement measurementLine
    ) {
        return "The package " + packageCode + " has been tampered with!" +
                "Received measurement " + measurement + " on package code: " + packageCode +
                "On: " + measurementLine.getTimestamp().toString();
    }

    private String buildDeliverySubject(long orderId) {
        return "Order Delivery | Order #" + orderId;
    }

    private String buildDeliveryText(
            Order order
    ) {
        StringBuilder message = new StringBuilder();
        message.append("The order has been delivered to ").append(order.getAddress()).append("\n")
                .append("With the items: \n");
        for (OrderItem item : order.getOrderItems()) {
            message.append("- ").append(item.getProduct().getName())
                    .append(", Quantity: ").append(item.getQuantity())
                    .append("\n");
        }
        message.append("On: ").append(order.getDate().toString());
        return message.toString();
    }
}
