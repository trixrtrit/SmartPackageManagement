package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.validation.ConstraintViolationException;
import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.*;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.Package;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyEntityNotFoundException;

import java.util.List;

@Stateless
public class SensorBean {

    @PersistenceContext
    private EntityManager entityManager;

    public long create(

            String name,
            Long sensorTypeId
    ) throws MyEntityNotFoundException, MyConstraintViolationException {
        SensorType sensorType = entityManager.find(SensorType.class, sensorTypeId);
        if (sensorType == null){
            throw new MyEntityNotFoundException("SensorType with id '" + sensorTypeId + "' not found.");
        }
        try {
            Sensor sensor = new Sensor(name, sensorType);
            entityManager.persist(sensor);
            return sensor.getId();
        } catch (ConstraintViolationException err) {
            throw new MyConstraintViolationException(err);
        }
    }

    public List<Sensor> getSensors() {
        return entityManager.createNamedQuery("getSensors", Sensor.class).getResultList();
    }

    /*public Sensor getSensorMeasurements(Long id) throws MyEntityNotFoundException{
        if(!this.exists(id)) {
            throw new MyEntityNotFoundException("The sensor with the id: " + id + " does not exist");
        }
        Sensor sensor = entityManager.find(Sensor.class, id);
        Hibernate.initialize(sensor.getMeasurements());
        return sensor;
    }*/
    public boolean exists(Long id) {
        Query query = entityManager.createQuery(
                "SELECT COUNT(s.id) FROM Sensor s WHERE s.id = :id", Long.class
        );
        query.setParameter("id", id);
        return (Long) query.getSingleResult() > 0L;
    }

    public Sensor find(Long id) throws MyEntityNotFoundException {
        Sensor sensor = entityManager.find(Sensor.class, id);
        if (sensor == null) {
            throw new MyEntityNotFoundException("The sensor with the id: " + id + " does not exist");
        }
        Hibernate.initialize(sensor.getSensorPackageList());
        return sensor;
    }

    public void update(
            long id,
            String name,
            Long sensorTypeId
    ) throws MyEntityNotFoundException {
        Sensor sensor = this.find(id);
        SensorType sensorType = entityManager.find(SensorType.class, sensorTypeId);
        if (sensorType == null) {
            throw new MyEntityNotFoundException("The sensor type with the id: " + id + " does not exist");
        }
        entityManager.lock(sensor, LockModeType.OPTIMISTIC);
        sensor.setName(name);
        sensor.setSensorType(sensorType);
    }

    public Sensor delete(long id) throws MyEntityNotFoundException {
        Sensor sensor = this.find(id);
        sensor.setAvailable(false);
        entityManager.remove(sensor);
        return sensor;
    }


}
