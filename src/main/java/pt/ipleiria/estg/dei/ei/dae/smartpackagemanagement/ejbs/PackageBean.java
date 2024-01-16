package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.ejbs;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.validation.ConstraintViolationException;
import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.*;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.Package;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.enums.PackageType;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyPackageProductAssociationViolationException;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Stateless
public class PackageBean {
    @PersistenceContext
    private EntityManager entityManager;
    @EJB
    private QueryBean<Package> packageQueryBean;

    public long create(long code, String material, PackageType packageType)
            throws MyEntityNotFoundException, MyConstraintViolationException, MyEntityExistsException {
        if (exists(code)) {
            throw new MyEntityExistsException("A package with the code: " + code + " already exists");
        }
        try {
            Package aPackage = new Package(code, material, packageType);
            entityManager.persist(aPackage);
            return aPackage.getCode();
        } catch (ConstraintViolationException err) {
            throw new MyConstraintViolationException(err);
        }
    }

    public List<Package> getPackages(Map<String, String> filterMap, int pageNumber, int pageSize)
            throws IllegalArgumentException {
        Map<String, String> orderMap = new LinkedHashMap<>();
        orderMap.put("code", "asc");
        orderMap.put("packageType", "asc");
        return packageQueryBean.getEntities(Package.class, filterMap, orderMap, pageNumber, pageSize);
    }

    public long getPackagesCount(Map<String, String> filterMap) {
        return packageQueryBean.getEntitiesCount(Package.class, filterMap);
    }

    public Package getPackageProducts(long code) throws MyEntityNotFoundException {
        if(!this.exists(code)) {
            throw new MyEntityNotFoundException("The package with the code: " + code + " does not exist");
        }
        Package aPackage = entityManager.find(Package.class, code);
        Hibernate.initialize(aPackage.getProducts());
        return aPackage;
    }

    public Package getPackageSensors(long code) throws MyEntityNotFoundException {
        if(!this.exists(code)) {
            throw new MyEntityNotFoundException("The package with the code: " + code + " does not exist");
        }
        Package aPackage = entityManager.find(Package.class, code);
        Hibernate.initialize(aPackage.getSensorPackageList());
        return aPackage;
    }

    public Package getPackageMeasurements(long code) throws MyEntityNotFoundException {
        if(!this.exists(code)) {
            throw new MyEntityNotFoundException("The package with the code: " + code + " does not exist");
        }
        Package aPackage = entityManager.find(Package.class, code);
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

    public Package find(long code) throws MyEntityNotFoundException {
        Package aPackage = entityManager.find(Package.class, code);
        if (aPackage == null) {
            throw new MyEntityNotFoundException("The package with the code: " + code + " does not exist");
        }
        Hibernate.initialize(aPackage.getProducts());
        Hibernate.initialize(aPackage.getSensorPackageList());
        return aPackage;
    }

    public boolean exists(long code) {
        Query query = entityManager.createNamedQuery("packageExists", Package.class);
        query.setParameter("code", code);
        return (Long) query.getSingleResult() > 0L;
    }

    public void update(long code, String material, PackageType packageType)
            throws MyEntityNotFoundException, MyConstraintViolationException {
        Package aPackage = this.find(code);
        entityManager.lock(aPackage, LockModeType.OPTIMISTIC);
        aPackage.setMaterial(material);
        aPackage.setPackageType(packageType);
    }

    public Package delete(long code) throws MyEntityNotFoundException {
        Package aPackage = this.find(code);
        aPackage.setActive(false);
        entityManager.remove(aPackage);
        return aPackage;
    }

    public void addProductToPackage(long code, long productId)
            throws MyEntityNotFoundException, MyPackageProductAssociationViolationException {
        Package aPackage = find(code);
        if (aPackage == null)
            throw new MyEntityNotFoundException("The package with the code: " + code + " does not exist");

        Product product = entityManager.find(Product.class, productId);
        if (product == null)
            throw new MyEntityNotFoundException("The product with the id: " + productId + " does not exist");
        if (aPackage.getPackageType() != PackageType.TERTIARY && !aPackage.getProducts().isEmpty()) {
            throw new MyPackageProductAssociationViolationException("Non-tertiary packages cannot have more than one product");
        }
        aPackage.addProduct(product);
        product.addPackage(aPackage);
    }

    public void removeProductFromPackage(long code, long productId) throws MyEntityNotFoundException {
        Package aPackage = find(code);
        if (aPackage == null)
            throw new MyEntityNotFoundException("The package with the code: " + code + " does not exist");

        Product product = entityManager.find(Product.class, productId);
        if (product == null)
            throw new MyEntityNotFoundException("The product with the id: " + productId + " does not exist");
        aPackage.removeProduct(product);
        product.removePackage(aPackage);
    }

    public void addSensorToPackage(long code, long sensorId) throws MyEntityNotFoundException, MyEntityExistsException {
        Package aPackage = find(code);
        Sensor sensor = entityManager.find(Sensor.class, sensorId);
        if (sensor == null)
            throw new MyEntityNotFoundException("The sensor with the id: " + sensorId + " does not exist");
        if(findSensorPackageActiveSensor(sensorId)) {
            throw new MyEntityExistsException("The sensor with id: " + sensorId + " is already associated to another package");
        }
        SensorPackage sensorPackage = new SensorPackage(sensor, aPackage, Instant.now());
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

    public void removeSensorFromPackage(long code, long sensorId) throws MyEntityNotFoundException {
        Package aPackage = find(code);
        Sensor sensor = entityManager.find(Sensor.class, sensorId);
        if (sensor == null)
            throw new MyEntityNotFoundException("The sensor with the id: " + sensorId + " does not exist");
        //get pivot object update time
        SensorPackage sensorPackage = findSensorPackage(code, sensorId);
        if (sensorPackage == null)
            throw new MyEntityNotFoundException("Sensor with id: " + sensorId +
                    " is not associated to the package code: " + code);
        sensorPackage.setRemovedAt(Instant.now());
        aPackage.getSensorPackageList().remove(sensorPackage);
        sensor.getSensorPackageList().remove(sensorPackage);
        sensor.setAvailable(true);
    }

    public void changeActiveStatus(long id) throws MyEntityNotFoundException{
        Package aPackage = this.find(id);
        entityManager.lock(aPackage, LockModeType.OPTIMISTIC);
        aPackage.setActive(!aPackage.isActive());
    }
}
