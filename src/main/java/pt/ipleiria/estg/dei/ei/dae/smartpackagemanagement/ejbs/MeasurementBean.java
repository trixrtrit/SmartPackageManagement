package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.ejbs;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.validation.ConstraintViolationException;
import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.*;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.Package;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.enums.MeasurementType;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyEntityNotFoundException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Stateless
public class MeasurementBean {
    @PersistenceContext
    private EntityManager entityManager;
    @EJB
    private NotificationBean notificationBean;

    public long create(String measurement, long packageCode, long sensorId)
            throws MyConstraintViolationException, MyEntityNotFoundException {
        SensorPackage sensorPackage = findSensorPackage(packageCode, sensorId);
        try {
            var measurementLine = new Measurement(measurement, sensorPackage);
            entityManager.persist(measurementLine);
            entityManager.flush();
            processPackage(measurement, measurementLine, packageCode);
            return measurementLine.getId();
        } catch (ConstraintViolationException err) {
            throw new MyConstraintViolationException(err);
        }
    }

    private void processPackage(String measurement, Measurement measurementLine, long packageCode)
            throws MyConstraintViolationException {
        Package currentPackage = measurementLine.getSensorPackage().getaPackage();
        MeasurementType measurementType = measurementLine.getSensorPackage().getSensor().getSensorType().getMeasurementType();
        switch (measurementType) {
            case NUMERIC:
                processNumericMeasurements(currentPackage, measurement, measurementLine, packageCode);
                break;
            case BOOLEAN:
                if (Boolean.parseBoolean(measurement)) {
                    notificationBean.fireSecurityNotification(measurement, measurementLine, packageCode);
                }
                break;
        }
    }

    private void processNumericMeasurements(
            Package currentPackage,
            String measurement,
            Measurement measurementLine,
            long packageCode
    ) throws MyConstraintViolationException {
        if (currentPackage instanceof StandardPackage) {
            List<StandardPackageProduct> standardPackageProducts = ((StandardPackage) currentPackage).getStandardPackageProducts();
            for (StandardPackageProduct standardPackageProduct : standardPackageProducts) {
                processProductParameters(measurement, measurementLine, packageCode, standardPackageProduct);
            }
        }
    }

    private void processProductParameters(String measurement, Measurement measurementLine, long packageCode, StandardPackageProduct standardPackageProduct)
            throws MyConstraintViolationException {
        List<ProductParameter> productParameters = standardPackageProduct.getProduct().getProductParameters();
        for (ProductParameter productParameter : productParameters) {
            if (Float.compare(productParameter.getMinValue(), Float.parseFloat(measurement)) > 0 ||
                    Float.compare(productParameter.getMaxValue(), Float.parseFloat(measurement)) < 0) {
                notificationBean.fireEnvironmentalNotification(
                        measurement,
                        measurementLine,
                        standardPackageProduct.getProduct(),
                        productParameter,
                        packageCode
                );
            }
        }
    }

    private SensorPackage findSensorPackage(long packageCode, long sensorId) throws MyEntityNotFoundException {
        try {
            SensorPackage sensorPackage = entityManager.createNamedQuery("findSensorPackage", SensorPackage.class)
                    .setParameter("packageCode", packageCode)
                    .setParameter("sensorId", sensorId)
                    .getSingleResult();
            if (sensorPackage.getaPackage() instanceof StandardPackage) {
                Hibernate.initialize(((StandardPackage) sensorPackage.getaPackage()).getStandardPackageProducts());
            }
            return sensorPackage;
        } catch (NoResultException e) {
            throw new MyEntityNotFoundException("Sensor with id: " + sensorId +
                    " is not associated to the package code: " + packageCode);
        }
    }

    public List<Measurement> getMeasurements(
            Long sensorId,
            String packageCode,
            Date startDate,
            Date endDate,
            Boolean isActive,
            int pageNumber,
            int pageSize
    ) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Measurement> query = builder.createQuery(Measurement.class);
        Root<Measurement> root = query.from(Measurement.class);

        List<Predicate> predicates = getPredicates(sensorId, packageCode, startDate, endDate, isActive, builder, root);

        query.where(builder.and(predicates.toArray(new Predicate[0])));

        query.orderBy(builder.asc(root.get("sensorPackage").get("sensor").get("id")),
                builder.asc(root.get("sensorPackage").get("sensor").get("name")),
                builder.asc(root.get("sensorPackage").get("aPackage").get("code")),
                builder.asc(root.get("sensorPackage").get("addedAt")));

        var measurements = entityManager.createQuery(query).
                setFirstResult((pageNumber - 1) * pageSize).
                setMaxResults(pageSize).getResultList();
        for (Measurement measurement : measurements) {
            Package pkg = measurement.getSensorPackage().getaPackage();
            if (pkg instanceof StandardPackage) {
                StandardPackage standardPkg = (StandardPackage) pkg;
                Hibernate.initialize(standardPkg.getStandardPackageProducts());
                for (StandardPackageProduct standardPackageProduct : standardPkg.getStandardPackageProducts()) {
                    Hibernate.initialize(standardPackageProduct.getProduct().getProductParameters());
                }
            }
        }
        return measurements;
    }

    public long getMeasurementsCount(
            Long sensorId,
            String packageCode,
            Date startDate,
            Date endDate,
            Boolean isActive
    ) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = builder.createQuery(Long.class);
        Root<Measurement> root = query.from(Measurement.class);

        List<Predicate> predicates = getPredicates(sensorId, packageCode, startDate, endDate, isActive, builder, root);

        query.select(builder.count(root));
        query.where(builder.and(predicates.toArray(new Predicate[0])));

        return entityManager.createQuery(query).getSingleResult();
    }

    public Measurement find(long measurementId) throws MyEntityNotFoundException {
        Measurement measurement = entityManager.find(Measurement.class, measurementId);
        if (measurement == null) {
            throw new MyEntityNotFoundException("The measurement with the id: " + measurementId + " does not exist");
        }
        if (measurement.getSensorPackage().getaPackage() instanceof StandardPackage) {
            Hibernate.initialize(((StandardPackage) measurement.getSensorPackage().getaPackage()).getStandardPackageProducts());
        }
        return measurement;
    }

    private static List<Predicate> getPredicates(Long sensorId, String packageCode, Date startDate, Date endDate, Boolean isActive, CriteriaBuilder builder, Root<Measurement> root) {
        List<Predicate> predicates = new ArrayList<>();

        if (sensorId != null) {
            predicates.add(builder.equal(root.get("sensorPackage").get("sensor").get("id"), sensorId));
        }

        if (packageCode != null) {
            predicates.add(builder.equal(root.get("sensorPackage").get("aPackage").get("code"), packageCode));
        }

        if (startDate != null && endDate != null) {
            predicates.add(builder.between(root.get("sensorPackage").get("addedAt"), startDate, endDate));
        } else if (startDate != null) {
            predicates.add(builder.greaterThanOrEqualTo(root.get("sensorPackage").get("addedAt"), startDate));
        } else if (endDate != null) {
            predicates.add(builder.lessThanOrEqualTo(root.get("sensorPackage").get("addedAt"), endDate));
        }

        if (isActive != null && isActive) {
            predicates.add(builder.isNull(root.get("sensorPackage").get("removedAt")));
        }
        return predicates;
    }
}
