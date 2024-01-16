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
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.Customer;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.security.Hasher;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Stateless
public class CustomerBean {
    @PersistenceContext
    private EntityManager entityManager;
    @Inject
    private Hasher hasher;
    @EJB
    private QueryBean<Customer> customerQueryBean;

    public void create(String username, String password, String name, String email, String nif, String address)
            throws MyEntityExistsException, MyConstraintViolationException {
        if (exists(username)) {
            throw new MyEntityExistsException("A customer with the username: " + username + " already exists");
        }
        try {
            Customer customer = new Customer(username, hasher.hash(password), name, email, nif, address);
            entityManager.persist(customer);
        } catch (ConstraintViolationException err) {
            throw new MyConstraintViolationException(err);
        }
    }

    public List<Customer> getCustomers(Map<String, String> filterMap, int pageNumber, int pageSize)
            throws IllegalArgumentException {
        Map<String, String> orderMap = new LinkedHashMap<>();
        orderMap.put("name", "asc");
        orderMap.put("username", "asc");
        return customerQueryBean.getEntities(Customer.class, filterMap, orderMap, pageNumber, pageSize);
    }

    public long getCustomersCount(Map<String, String> filterMap) {
        return customerQueryBean.getEntitiesCount(Customer.class, filterMap);
    }

    public Customer getCustomerOrders(String username) throws MyEntityNotFoundException{
        if(!this.exists(username)) {
            throw new MyEntityNotFoundException("The customer with the username: " + username + " does not exist");
        }
        Customer customer = entityManager.find(Customer.class, username);
        Hibernate.initialize(customer.getOrders());
        return customer;
    }

    public boolean exists(String username) {
        Query query = entityManager.createNamedQuery("customerExists", Customer.class);
        query.setParameter("username", username);
        return (Long) query.getSingleResult() > 0L;
    }

    public Customer find(String username) throws MyEntityNotFoundException {
        Customer customer = entityManager.find(Customer.class, username);
        if (customer == null) {
            throw new MyEntityNotFoundException("The customer with the username: " + username + " does not exist");
        }
        Hibernate.initialize(customer.getOrders());
        return customer;
    }

    public void update(String username, String name, String email, String nif, String address) throws MyEntityNotFoundException {
        Customer customer = this.find(username);
        entityManager.lock(customer, LockModeType.OPTIMISTIC);
        customer.setAddress(address);
        customer.setNif(nif);
        customer.setName(name);
        customer.setEmail(email);
    }

    public Customer delete(String username) throws MyEntityNotFoundException {
        Customer customer = this.find(username);
        entityManager.remove(customer);
        return customer;
    }
}
