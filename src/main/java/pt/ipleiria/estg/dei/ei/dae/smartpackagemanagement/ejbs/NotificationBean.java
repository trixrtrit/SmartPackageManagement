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
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.enums.MeasurementType;
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
        String subject = buildEnvironmentalSubject(product);;
        String text= buildEnvironmentalText(product, productParameter, measurement, packageCode, measurementLine);
        emailBean.send(product.getManufacturer().getEmail(), subject, text);
        try {
            Notification notification = new Notification(text, product.getManufacturer(), measurementLine);
            entityManager.persist(notification);
            Query query = entityManager.createNamedQuery("findCustomerPackage", Package.class).
                    setParameter("code", packageCode).setParameter("status", DeliveryStatus.DISPATCHED);
            if (!query.getResultList().isEmpty()) {
                Customer customer = (Customer) query.getResultList().get(0);
                emailBean.send(
                        customer.getEmail(),
                        subject,
                        text
                );
                notification = new Notification(text, customer, measurementLine);
                entityManager.persist(notification);
            }
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
            //Notification notification = new Notification(text, product.getManufacturer(), measurementLine);
            //entityManager.persist(notification);
            Query query = entityManager.createNamedQuery("findCustomerPackage", Package.class).
                    setParameter("code", packageCode).setParameter("status", DeliveryStatus.DISPATCHED);
            if (!query.getResultList().isEmpty()) {
                Customer customer = (Customer) query.getResultList().get(0);
                emailBean.send(
                        customer.getEmail(),
                        subject,
                        text
                );
                Notification notification = new Notification(text, customer, measurementLine);
                entityManager.persist(notification);
            }
        } catch (ConstraintViolationException err) {
            throw new MyConstraintViolationException(err);
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

    private String buildSecurityText(
            String measurement,
            long packageCode,
            Measurement measurementLine
    ) {
        return "The package " + packageCode + " has been tampered with!" +
                "Received measurement " + measurement + " on package code: " + packageCode +
                "On: " + measurementLine.getTimestamp().toString();
    }

    private String buildSecuritySubject(long packageCode) {
        return "Quality Control | Security Warning on package: " + packageCode;
    }
}
