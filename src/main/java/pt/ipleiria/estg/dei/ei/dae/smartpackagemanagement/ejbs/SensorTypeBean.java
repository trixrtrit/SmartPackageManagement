package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.ConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.SensorType;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyEntityNotFoundException;

import java.util.List;

@Stateless
public class SensorTypeBean {
    @PersistenceContext
    private EntityManager entityManager;

    public long create(String name, String measurementType)
            throws MyEntityExistsException, MyConstraintViolationException, MyEntityNotFoundException {

        if (exists(name)) {
            throw new MyEntityExistsException("Sensor type with the name '" + name + " already exists.");
        }
        try {
            var sensorType = new SensorType(name, measurementType);
            entityManager.persist(sensorType);
            return sensorType.getId();
        } catch (ConstraintViolationException err) {
            throw new MyConstraintViolationException(err);
        }
    }

    public List<SensorType> getProductParameters() {
        return entityManager.createNamedQuery("getSensorTypes", SensorType.class).getResultList();
    }

    public boolean exists(String name) {
        var query = entityManager.createQuery(
                "SELECT COUNT(*) FROM SensorType st WHERE st.name = :name", Long.class
        );
        query.setParameter("name", name);
        return (Long) query.getSingleResult() > 0L;
    }

    public SensorType find(long id) throws MyEntityNotFoundException {
        var sensorType = entityManager.find(SensorType.class, id);
        if (sensorType == null) {
            throw new MyEntityNotFoundException("Sensor type with id: '" + id + "' doest not exist.");
        }
        return sensorType;
    }

}
