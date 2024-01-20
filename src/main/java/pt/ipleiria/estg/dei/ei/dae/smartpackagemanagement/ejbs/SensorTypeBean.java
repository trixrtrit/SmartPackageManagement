package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.ejbs;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.ConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.SensorType;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.enums.MeasurementType;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyEntityNotFoundException;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Stateless
public class SensorTypeBean {
    @PersistenceContext
    private EntityManager entityManager;
    @EJB
    private QueryBean<SensorType> sensorTypeQueryBean;

    public long create(String name, String measurementUnit, MeasurementType measurementType)
            throws MyEntityExistsException, MyConstraintViolationException, MyEntityNotFoundException {

        if (exists(name)) {
            throw new MyEntityExistsException("Sensor type with the name '" + name + " already exists.");
        }
        try {
            var sensorType = new SensorType(name, measurementUnit, measurementType);
            entityManager.persist(sensorType);
            return sensorType.getId();
        } catch (ConstraintViolationException err) {
            throw new MyConstraintViolationException(err);
        }
    }

    public List<SensorType> getProductParameters(Map<String, String> filterMap, int pageNumber, int pageSize)
            throws IllegalArgumentException {
        Map<String, String> orderMap = new LinkedHashMap<>();
        orderMap.put("name", "asc");
        orderMap.put("measurementUnit", "asc");
        return sensorTypeQueryBean.getEntities(SensorType.class, filterMap, orderMap, pageNumber, pageSize);
    }

    public long getSensorTypeCount(Map<String, String> filterMap) {
        return sensorTypeQueryBean.getEntitiesCount(SensorType.class, filterMap);
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

    public void update(long id, String name, String measurementUnit, MeasurementType measurementType)
            throws MyEntityNotFoundException, MyConstraintViolationException {
        var sensorType = this.find(id);
        entityManager.lock(sensorType, LockModeType.OPTIMISTIC);
        sensorType.setName(name);
        sensorType.setMeasurementUnit(measurementUnit);
        sensorType.setMeasurementType(measurementType);
    }

    public SensorType delete(long id) throws MyEntityNotFoundException {
        var sensorType = this.find(id);
        entityManager.remove(sensorType);
        return sensorType;
    }
}
