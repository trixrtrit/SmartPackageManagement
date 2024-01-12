package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.validation.ConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.*;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyEntityNotFoundException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Stateless
public class MeasurementBean {
    @PersistenceContext
    private EntityManager entityManager;

    public long create(double measurement, long packageCode, long sensorId)
            throws MyConstraintViolationException, MyEntityNotFoundException {
        SensorPackage sensorPackage = findSensorPackage(packageCode, sensorId);
        if (sensorPackage == null){
            throw new MyEntityNotFoundException("No sensor with id '" + sensorId + "' associated to package '"
                    + packageCode + "' has been found.");
        }
        try {
            var measurementLine = new Measurement(measurement, sensorPackage);
            measurementLine.setCreatedAt(new Date());
            entityManager.persist(measurementLine);
            return measurementLine.getId();
        } catch (ConstraintViolationException err) {
            throw new MyConstraintViolationException(err);
        }
    }

    public SensorPackage findSensorPackage(long packageCode, long sensorId){
        return entityManager.createQuery(
                        "SELECT sp FROM SensorPackage sp " +
                                "WHERE sp.aPackage.code = :packageId AND sp.sensor.id = :sensorId " +
                                "AND sp.removedAt IS NULL",
                        SensorPackage.class)
                .setParameter("packageId", packageCode)
                .setParameter("sensorId", sensorId)
                .getSingleResult();
    }

    public List<Measurement> getMeasurements(
            Long sensorId,
            String packageCode,
            Date startDate,
            Date endDate,
            Boolean isActive
    ) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Measurement> query = builder.createQuery(Measurement.class);
        Root<Measurement> root = query.from(Measurement.class);

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

        if(isActive) {
            predicates.add(builder.isNull(root.get("sensorPackage").get("removedAt")));
        }

        query.where(builder.and(predicates.toArray(new Predicate[0])));

        query.orderBy(builder.asc(root.get("sensorPackage").get("sensor").get("id")),
                builder.asc(root.get("sensorPackage").get("sensor").get("name")),
                builder.asc(root.get("sensorPackage").get("aPackage").get("code")),
                builder.asc(root.get("sensorPackage").get("addedAt")));

        return entityManager.createQuery(query).getResultList();
    }
}
