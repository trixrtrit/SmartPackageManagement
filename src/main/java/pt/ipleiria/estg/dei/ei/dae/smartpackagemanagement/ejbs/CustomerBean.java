package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.ejbs;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.validation.ConstraintViolationException;
import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.Customer;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.Order;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.security.Hasher;

import java.util.List;

@Stateless
public class CustomerBean {
    @PersistenceContext
    private EntityManager entityManager;
    @Inject
    private Hasher hasher;

    public void create(String username, String password, String name, String email, String nif, String address)
            throws MyEntityExistsException, MyConstraintViolationException {
        if (isExistingCustomer(username)) {
            throw new MyEntityExistsException("A customer with the username: " + username + " already exists");
        }
        try {
            Customer customer = new Customer(username, hasher.hash(password), name, email, nif, address);
            entityManager.persist(customer);
        } catch (ConstraintViolationException err) {
            throw new MyConstraintViolationException(err);
        }
    }

    public List<Customer> getCustomers() {
        return entityManager.createNamedQuery("getCustomers", Customer.class).getResultList();
    }

    public Customer getCustomerOrders(String username) throws MyEntityNotFoundException{
        if(!this.isExistingCustomer(username)) {
            throw new MyEntityNotFoundException("The customer with the username: " + username + " does not exist");
        }
        Customer customer = entityManager.find(Customer.class, username);
        Hibernate.initialize(customer.getOrders());
        return customer;
    }

    public boolean isExistingCustomer(String username) {
        Query query = entityManager.createQuery(
                "SELECT COUNT(c.username) FROM Customer c WHERE c.username = :username", Long.class
        );
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
