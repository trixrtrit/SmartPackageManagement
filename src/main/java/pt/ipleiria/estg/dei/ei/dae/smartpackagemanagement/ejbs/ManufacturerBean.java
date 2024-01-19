package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.ejbs;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.validation.ConstraintViolationException;
import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.Manufacturer;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.security.Hasher;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Stateless
public class ManufacturerBean {

    @PersistenceContext
    private EntityManager entityManager;
    @Inject
    private Hasher hasher;
    @EJB
    private QueryBean<Manufacturer> manufacturerQueryBean;

    public void create(String username, String password, String name, String email)
            throws MyEntityExistsException, MyConstraintViolationException {
        if (exists(username)) {
            throw new MyEntityExistsException("A manufacturer with the username: " + username + " already exists");
        }
        try {
            Manufacturer manufacturer = new Manufacturer(username, hasher.hash(password), name, email);
            entityManager.persist(manufacturer);
        } catch (ConstraintViolationException err) {
            throw new MyConstraintViolationException(err);
        }
    }

    public List<Manufacturer> getManufacturers(Map<String, String> filterMap, int pageNumber, int pageSize)
            throws IllegalArgumentException {
        Map<String, String> orderMap = new LinkedHashMap<>();
        orderMap.put("name", "asc");
        orderMap.put("username", "asc");
        return manufacturerQueryBean.getEntities(Manufacturer.class, filterMap, orderMap, pageNumber, pageSize);
    }

    public long getManufacturersCount(Map<String, String> filterMap) {
        return manufacturerQueryBean.getEntitiesCount(Manufacturer.class, filterMap);
    }

    public Manufacturer getManufacturerProducts(String username) throws MyEntityNotFoundException {
        if(!this.exists(username)) {
            throw new MyEntityNotFoundException("The manufacturer with the username: " + username + " does not exist");
        }

        Manufacturer manufacturer = entityManager.find(Manufacturer.class, username);
        Hibernate.initialize(manufacturer.getProducts());
        return manufacturer;
    }

    public boolean exists(String username) {
        Query query = entityManager.createNamedQuery("manufacturerExists", Manufacturer.class);
        query.setParameter("username", username);
        return (Long) query.getSingleResult() > 0L;
    }

    public Manufacturer find(String username) throws MyEntityNotFoundException {
        Manufacturer manufacturer = entityManager.find(Manufacturer.class, username);
        if (manufacturer == null) {
            throw new MyEntityNotFoundException("The manufacturer with the username: " + username + " does not exist");
        }
        Hibernate.initialize(manufacturer.getProducts());
        return manufacturer;
    }

    public void update(String username, String name, String email) throws MyEntityNotFoundException {
        Manufacturer manufacturer = this.find(username);
        entityManager.lock(manufacturer, LockModeType.OPTIMISTIC);
        manufacturer.setName(name);
        manufacturer.setEmail(email);
    }

    public Manufacturer delete(String username) throws MyEntityNotFoundException {
        Manufacturer manufacturer = this.find(username);
        entityManager.remove(manufacturer);
        return manufacturer;
    }
}
