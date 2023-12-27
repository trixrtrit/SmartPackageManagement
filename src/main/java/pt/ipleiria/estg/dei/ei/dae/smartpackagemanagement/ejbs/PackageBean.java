package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.validation.ConstraintViolationException;
import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.Package;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.enums.PackageType;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyEntityNotFoundException;

import java.util.List;

@Stateless
public class PackageBean {
    @PersistenceContext
    private EntityManager entityManager;

    public long create(String material, PackageType packageType)
            throws MyEntityNotFoundException, MyConstraintViolationException {
        try {
            Package aPackage = new Package(material, packageType);
            entityManager.persist(aPackage);
            return aPackage.getId();
        } catch (ConstraintViolationException err) {
            throw new MyConstraintViolationException(err);
        }
    }

    public List<Package> getPackages() {
        return entityManager.createNamedQuery("getPackages", Package.class).getResultList();
    }

    public Package getPackageProducts(long id) throws MyEntityNotFoundException {
        if(!this.exists(id)) {
            throw new MyEntityNotFoundException("The package with the id: " + id + " does not exist");
        }
        Package aPackage = entityManager.find(Package.class, id);
        Hibernate.initialize(aPackage.getProducts());
        return aPackage;
    }

    public Package getPackageSensors(long id) throws MyEntityNotFoundException {
        if(!this.exists(id)) {
            throw new MyEntityNotFoundException("The package with the id: " + id + " does not exist");
        }
        Package aPackage = entityManager.find(Package.class, id);
        Hibernate.initialize(aPackage.getSensors());
        return aPackage;
    }

    public Package find(long id) throws MyEntityNotFoundException {
        Package aPackage = entityManager.find(Package.class, id);
        if (aPackage == null) {
            throw new MyEntityNotFoundException("The package with the id: " + id + " does not exist");
        }
        Hibernate.initialize(aPackage.getProducts());
        Hibernate.initialize(aPackage.getSensors());
        return aPackage;
    }

    public boolean exists(long id) {
        Query query = entityManager.createNamedQuery("packageExists", Package.class);
        query.setParameter("id", id);
        return (Long) query.getSingleResult() > 0L;
    }

    public void update(long id, String material, PackageType packageType)
            throws MyEntityNotFoundException, MyConstraintViolationException {
        Package aPackage = this.find(id);
        entityManager.lock(aPackage, LockModeType.OPTIMISTIC);
        aPackage.setMaterial(material);
        aPackage.setPackageType(packageType);
    }

    public Package delete(long id) throws MyEntityNotFoundException {
        Package aPackage = this.find(id);
        entityManager.remove(aPackage);
        return aPackage;
    }
}
