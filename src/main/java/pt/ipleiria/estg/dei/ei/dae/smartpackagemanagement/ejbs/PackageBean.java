package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.ejbs;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.*;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.Package;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyEntityNotFoundException;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Stateless
public class PackageBean {
    @PersistenceContext
    private EntityManager entityManager;
    @EJB
    private QueryBean<Package> packageQueryBean;

    public List<Package> getPackages(Map<String, String> filterMap, int pageNumber, int pageSize)
            throws IllegalArgumentException {
        Map<String, String> orderMap = new LinkedHashMap<>();
        orderMap.put("code", "asc");
        return packageQueryBean.getEntities(Package.class, filterMap, orderMap, pageNumber, pageSize);
    }

    public long getPackagesCount(Map<String, String> filterMap) {
        return packageQueryBean.getEntitiesCount(Package.class, filterMap);
    }

    public Package getPackageMeasurements(long code, Class<? extends Package> pkgType) throws MyEntityNotFoundException {
        if(!this.exists(code, pkgType)) {
            throw new MyEntityNotFoundException("The package with the code: " + code + " does not exist");
        }
        Package aPackage = entityManager.find(pkgType, code);
        Hibernate.initialize(aPackage.getSensorPackageList());
        aPackage.getSensorPackageList().forEach(sensorPackage -> Hibernate.initialize(sensorPackage.getMeasurements()));
        return aPackage;
    }

    public List<Sensor> findPackageCurrentSensors(long packageCode){
        return entityManager.createQuery(
                        "SELECT sp.sensor FROM SensorPackage sp WHERE sp.aPackage.code = :packageCode " +
                                "AND sp.removedAt IS NULL",
                        Sensor.class)
                .setParameter("packageCode", packageCode)
                .getResultList();
    }

    public SensorPackage findSensorPackage(long packageCode, long sensorId){
        return entityManager.createNamedQuery("findSensorPackage", SensorPackage.class)
                .setParameter("packageCode", packageCode)
                .setParameter("sensorId", sensorId)
                .getSingleResult();
    }

    public Package find(long code, Class<? extends Package> pkgType) throws MyEntityNotFoundException {
        Package aPackage = entityManager.find(pkgType, code);
        if (aPackage == null) {
            throw new MyEntityNotFoundException("The package with the code: " + code + " does not exist");
        }
        Hibernate.initialize(aPackage.getSensorPackageList());
        return aPackage;
    }

    public boolean exists(long code, Class<? extends Package> pkgType) {
        Query query = entityManager.createNamedQuery("packageExists", pkgType);
        query.setParameter("code", code);
        return (Long) query.getSingleResult() > 0L;
    }

    public Package delete(long code, Class<? extends Package> pkgType) throws MyEntityNotFoundException {
        Package aPackage = this.find(code, pkgType);
        aPackage.setActive(false);
        entityManager.remove(aPackage);
        return aPackage;
    }

    public void addSensorToPackage(long code, long sensorId, Class<? extends Package> pkgType) throws MyEntityNotFoundException, MyEntityExistsException {
        Package aPackage = find(code, pkgType);
        Sensor sensor = entityManager.find(Sensor.class, sensorId);
        if (sensor == null)
            throw new MyEntityNotFoundException("The sensor with the id: " + sensorId + " does not exist");
        if(findSensorPackageActiveSensor(sensorId)) {
            throw new MyEntityExistsException("The sensor with id: " + sensorId + " is already associated to another package");
        }
        SensorPackage sensorPackage = new SensorPackage(sensor, aPackage);
        aPackage.getSensorPackageList().add(sensorPackage);
        sensor.getSensorPackageList().add(sensorPackage);
        sensor.setAvailable(false);
        entityManager.persist(sensorPackage);
    }

    private boolean findSensorPackageActiveSensor(long sensorId){
        Query query = entityManager.createNamedQuery("sensorPackageExists", SensorPackage.class)
                .setParameter("sensorId", sensorId);
        return (Long) query.getSingleResult() > 0L;
    }

    public void removeSensorFromPackage(long code, long sensorId, Class<? extends Package> pkgType)
            throws MyEntityNotFoundException {
        Package aPackage = find(code, pkgType);
        Sensor sensor = entityManager.find(Sensor.class, sensorId);
        if (sensor == null)
            throw new MyEntityNotFoundException("The sensor with the id: " + sensorId + " does not exist");
        //get pivot object update time
        SensorPackage sensorPackage = findSensorPackage(code, sensorId);
        if (sensorPackage == null)
            throw new MyEntityNotFoundException("Sensor with id: " + sensorId +
                    " is not associated to the package code: " + code);
        sensorPackage.setRemovedAt(new Date());
        aPackage.getSensorPackageList().remove(sensorPackage);
        sensor.getSensorPackageList().remove(sensorPackage);
        sensor.setAvailable(true);
    }

    public void changeActiveStatus(long id, Class<? extends Package> pkgType) throws MyEntityNotFoundException{
        Package aPackage = this.find(id, pkgType);
        entityManager.lock(aPackage, LockModeType.OPTIMISTIC);
        aPackage.setActive(!aPackage.isActive());
    }
}
