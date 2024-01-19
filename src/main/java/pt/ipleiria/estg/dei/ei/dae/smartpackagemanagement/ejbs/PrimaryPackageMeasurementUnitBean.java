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
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyIllegalConstraintException;

import java.util.List;

@Stateless
public class PrimaryPackageMeasurementUnitBean {
    @PersistenceContext
    private EntityManager entityManager;
    @EJB
    private QueryBean<PrimaryPackageMeasurementUnit> primaryPackageMeasurementUnitQueryBean;

    public long create(String unit)
            throws MyEntityExistsException, MyConstraintViolationException, MyEntityNotFoundException {

        if (exists(unit)) {
            throw new MyEntityExistsException("Primary package measurement unit '" + unit + " already exists.");
        }
        try {
            var primaryPackageType = new PrimaryPackageMeasurementUnit(unit);
            entityManager.persist(primaryPackageType);
            return primaryPackageType.getId();
        } catch (ConstraintViolationException err) {
            throw new MyConstraintViolationException(err);
        }
    }
    public boolean exists(String unit) {
        var query = entityManager.createQuery(
                "SELECT COUNT(*) FROM PrimaryPackageMeasurementUnit st WHERE st.unit = :unit", Long.class
        );
        query.setParameter("unit", unit);
        return (Long) query.getSingleResult() > 0L;
    }

    public List<PrimaryPackageMeasurementUnit> getUnits(){
        Query query = entityManager.createNamedQuery("getPrimaryPackageMeasurementUnits", PrimaryPackageMeasurementUnit.class);
        var result = query.getResultList();

        return result;
    }

    public PrimaryPackageMeasurementUnit find(long id) throws MyEntityNotFoundException {
        var primaryPackageMeasurementUnit = entityManager.find(PrimaryPackageMeasurementUnit.class, id);
        if (primaryPackageMeasurementUnit == null) {
            throw new MyEntityNotFoundException("Primary package measurement unit with id: '" + id + "' doest not exist.");
        }
        return primaryPackageMeasurementUnit;
    }

    public void update(long id, String unit) throws MyEntityNotFoundException, MyConstraintViolationException {
        var primaryPackageMeasurementUnit = this.find(id);
        entityManager.lock(primaryPackageMeasurementUnit, LockModeType.OPTIMISTIC);
        primaryPackageMeasurementUnit.setUnit(unit);
    }

    public PrimaryPackageMeasurementUnit delete(long id) throws MyEntityNotFoundException, MyIllegalConstraintException {
        var primaryPackageMeasurementUnit = this.find(id);

        Hibernate.initialize(primaryPackageMeasurementUnit.getProducts());

        if (primaryPackageMeasurementUnit.getProducts().stream().count() > 0){
            throw new MyIllegalConstraintException("Cannot delete primary package measurement unit that still has products attached.");
        }

        entityManager.remove(primaryPackageMeasurementUnit);
        return primaryPackageMeasurementUnit;
    }
}
