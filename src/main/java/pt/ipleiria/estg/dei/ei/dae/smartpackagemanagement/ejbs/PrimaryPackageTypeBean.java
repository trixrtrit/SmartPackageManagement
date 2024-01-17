package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.ejbs;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.validation.ConstraintViolationException;
import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.PrimaryPackageMeasurementUnit;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.PrimaryPackageType;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyIllegalConstraintException;

import java.util.List;

@Stateless
public class PrimaryPackageTypeBean {
    @PersistenceContext
    private EntityManager entityManager;
    @EJB
    private QueryBean<PrimaryPackageMeasurementUnit> primaryPackageMeasurementUnitQueryBean;

    public long create(String type)
            throws MyEntityExistsException, MyConstraintViolationException, MyEntityNotFoundException {

        if (exists(type)) {
            throw new MyEntityExistsException("Primary package type '" + type + " already exists.");
        }
        try {
            var primaryPackageType = new PrimaryPackageType(type);
            entityManager.persist(primaryPackageType);
            return primaryPackageType.getId();
        } catch (ConstraintViolationException err) {
            throw new MyConstraintViolationException(err);
        }
    }

    public List<PrimaryPackageType> getTypes(){
        Query query = entityManager.createNamedQuery("getPrimaryPackageTypes", PrimaryPackageType.class);
        var result = query.getResultList();

        return result;
    }

    public boolean exists(String type) {
        var query = entityManager.createQuery(
                "SELECT COUNT(*) FROM PrimaryPackageType st WHERE st.type = :type", Long.class
        );
        query.setParameter("type", type);
        return (Long) query.getSingleResult() > 0L;
    }

    public PrimaryPackageType find(long id) throws MyEntityNotFoundException {
        var primaryPackageType = entityManager.find(PrimaryPackageType.class, id);
        if (primaryPackageType == null) {
            throw new MyEntityNotFoundException("Primary package type with id: '" + id + "' doest not exist.");
        }
        return primaryPackageType;
    }

    public void update(long id, String type) throws MyEntityNotFoundException, MyConstraintViolationException {
        var primaryPackageMeasurementUnit = this.find(id);
        entityManager.lock(primaryPackageMeasurementUnit, LockModeType.OPTIMISTIC);
        primaryPackageMeasurementUnit.setType(type);
    }

    public PrimaryPackageType delete(long id) throws MyEntityNotFoundException, MyIllegalConstraintException {
        var primaryPackageMeasurementUnit = this.find(id);

        Hibernate.initialize(primaryPackageMeasurementUnit.getProducts());

        if (primaryPackageMeasurementUnit.getProducts().stream().count() > 0){
            throw new MyIllegalConstraintException("Cannot delete primary package type that still has products attached.");
        }

        entityManager.remove(primaryPackageMeasurementUnit);
        return primaryPackageMeasurementUnit;
    }
}
