package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.validation.ConstraintViolationException;
import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.*;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.Package;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyEntityNotFoundException;

import java.time.Instant;
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
            measurementLine.setTimestamp(Instant.now());
            entityManager.persist(measurementLine);
            return measurementLine.getId();
        } catch (ConstraintViolationException err) {
            throw new MyConstraintViolationException(err);
        }
    }

    private SensorPackage findSensorPackage(long packageCode, long sensorId){
        return entityManager.createNamedQuery(
                        "findSensorPackage",
                        SensorPackage.class)
                .setParameter("packageCode", packageCode)
                .setParameter("sensorId", sensorId)
                .getSingleResult();
    }

    public List<Measurement> getMeasurements(
            Long sensorId,
            String packageCode,
            Instant startDate,
            Instant endDate,
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

        if(isActive != null && isActive) {
            predicates.add(builder.isNull(root.get("sensorPackage").get("removedAt")));
        }

        query.where(builder.and(predicates.toArray(new Predicate[0])));

        query.orderBy(builder.asc(root.get("sensorPackage").get("sensor").get("id")),
                builder.asc(root.get("sensorPackage").get("sensor").get("name")),
                builder.asc(root.get("sensorPackage").get("aPackage").get("code")),
                builder.asc(root.get("sensorPackage").get("addedAt")));

        return entityManager.createQuery(query).getResultList();
    }

    public Measurement find(long measurementId)  throws MyEntityNotFoundException {
        Measurement measurement = entityManager.find(Measurement.class, measurementId);
        if (measurement == null) {
            throw new MyEntityNotFoundException("The measurement with the id: " + measurementId + " does not exist");
        }
        return measurement;
    }
}
