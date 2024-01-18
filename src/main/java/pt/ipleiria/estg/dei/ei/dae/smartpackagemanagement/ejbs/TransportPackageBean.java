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
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyEntityNotFoundException;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Stateless
public class TransportPackageBean {
    @PersistenceContext
    private EntityManager entityManager;
    @EJB
    private PackageBean packageBean;
    @EJB
    private QueryBean<TransportPackage> transportPackageQueryBean;

    public long create(long code, String material)
            throws MyEntityNotFoundException, MyConstraintViolationException, MyEntityExistsException {
        if (exists(code)) {
            throw new MyEntityExistsException("A package with the code: " + code + " already exists");
        }
        try {
            TransportPackage transportPackage = new TransportPackage(code, material);
            entityManager.persist(transportPackage);
            return transportPackage.getCode();
        } catch (ConstraintViolationException err) {
            throw new MyConstraintViolationException(err);
        }
    }

    public List<TransportPackage> getTransportPackages(Map<String, String> filterMap, int pageNumber, int pageSize)
            throws IllegalArgumentException {
        Map<String, String> orderMap = new LinkedHashMap<>();
        orderMap.put("code", "asc");
        return transportPackageQueryBean.getEntities(TransportPackage.class, filterMap, orderMap, pageNumber, pageSize);
    }

    public long getTransportPackagesCount(Map<String, String> filterMap) {
        return transportPackageQueryBean.getEntitiesCount(TransportPackage.class, filterMap);
    }

    public TransportPackage find(long code) throws MyEntityNotFoundException {
        TransportPackage transportPackage = entityManager.find(TransportPackage.class, code);
        if (transportPackage == null) {
            throw new MyEntityNotFoundException("The package with the code: " + code + " does not exist");
        }
        Hibernate.initialize(transportPackage.getSensorPackageList());
        Hibernate.initialize(transportPackage.getTransportPackageStandardPackages());
        return transportPackage;
    }

    public TransportPackage update(long code, String material)
            throws MyEntityNotFoundException, MyConstraintViolationException {
        TransportPackage transportPackage = this.find(code);
        entityManager.lock(transportPackage, LockModeType.OPTIMISTIC);
        transportPackage.setMaterial(material);
        return transportPackage;
    }

    public TransportPackage getPackageSensors(long code) throws MyEntityNotFoundException {
        if(!this.exists(code)) {
            throw new MyEntityNotFoundException("The package with the code: " + code + " does not exist");
        }
        TransportPackage transportPackage = entityManager.find(TransportPackage.class, code);
        Hibernate.initialize(transportPackage.getSensorPackageList());
        return transportPackage;
    }

    public TransportPackage getTransportStandardPackages(long code) throws MyEntityNotFoundException {
        if(!this.exists(code)) {
            throw new MyEntityNotFoundException("The package with the code: " + code + " does not exist");
        }
        TransportPackage transportPackage = entityManager.find(TransportPackage.class, code);
        Hibernate.initialize(transportPackage.getTransportPackageStandardPackages());
        return transportPackage;
    }

    public boolean exists(long code) {
        Query query = entityManager.createNamedQuery("packageExists", TransportPackage.class);
        query.setParameter("code", code);
        return (Long) query.getSingleResult() > 0L;
    }

    public TransportPackageStandardPackage findTransportPackageStandardPackageAssociation(
            long transportPkgCode, long standardPkgCode
    ){
        return entityManager.createNamedQuery("findTransportPackageStandardPackageAssociation",
                        TransportPackageStandardPackage.class)
                .setParameter("transportPkgCode", transportPkgCode)
                .setParameter("standardPkgCode", standardPkgCode)
                .getSingleResult();
    }

    public void addSensorToPackage(long code, long sensorId) throws MyEntityNotFoundException, MyEntityExistsException {
        packageBean.addSensorToPackage(code, sensorId, TransportPackage.class);
    }

    public void removeSensorFromPackage(long code, long sensorId) throws MyEntityNotFoundException {
        packageBean.removeSensorFromPackage(code, sensorId, TransportPackage.class);
    }

    public void addStandardPkgToTransportPkg(long transportPkgCode, long standardPkgCode)
            throws MyEntityNotFoundException, MyEntityExistsException {
        TransportPackage transportPackage = find(transportPkgCode);
        StandardPackage standardPackage = entityManager.find(StandardPackage.class, standardPkgCode);
        if (standardPackage == null)
            throw new MyEntityNotFoundException("The package with the code: " + standardPkgCode + " does not exist");
        if(transportPackageStandardPackageAssociationExists(standardPkgCode)) {
            throw new MyEntityExistsException("The package with code: " + standardPkgCode + " is already" +
                    " associated with another transport package");
        }
        TransportPackageStandardPackage transportPackageStandardPackage =
                new TransportPackageStandardPackage(standardPackage, transportPackage);
        transportPackage.getTransportPackageStandardPackages().add(transportPackageStandardPackage);
        standardPackage.getTransportPackageStandardPackages().add(transportPackageStandardPackage);
        transportPackageStandardPackage.setAddedAt(new Date());
        entityManager.persist(transportPackageStandardPackage);
    }

    private boolean transportPackageStandardPackageAssociationExists(long standardPkgCode){
        Query query = entityManager.createNamedQuery("transportPackageStandardPackageAssociationExists",
                        TransportPackageStandardPackage.class)
                .setParameter("standardPkgCode", standardPkgCode);
        return (Long) query.getSingleResult() > 0L;
    }

    public void removeStandardPkgFromTransportPkg(long transportPkgCode, long standardPkgCode)
            throws MyEntityNotFoundException {
        TransportPackage transportPackage = find(transportPkgCode);
        StandardPackage standardPackage = entityManager.find(StandardPackage.class, standardPkgCode);
        if (standardPackage == null)
            throw new MyEntityNotFoundException("The package with the code: " + standardPkgCode + " does not exist");
        TransportPackageStandardPackage transportPackageStandardPackage =
                findTransportPackageStandardPackageAssociation(transportPkgCode, standardPkgCode);
        if (transportPackage == null)
            throw new MyEntityNotFoundException("Package with code: " + standardPkgCode +
                    " is not associated to the transport package code: " + transportPkgCode);
        transportPackageStandardPackage.setRemovedAt(new Date());
        transportPackage.getTransportPackageStandardPackages().remove(transportPackageStandardPackage);
        standardPackage.getTransportPackageStandardPackages().remove(transportPackageStandardPackage);
    }
}
