package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.ejbs;

import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.validation.ConstraintViolationException;
import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.LogisticsOperator;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.security.Hasher;

import java.util.List;

public class LogisticsOperatorBean {
    @PersistenceContext
    private EntityManager entityManager;
    @Inject
    private Hasher hasher;

    public void create(String username, String password, String name, String email)
            throws MyEntityExistsException, MyConstraintViolationException {
        if (exists(username)) {
            throw new MyEntityExistsException("A logistics operator with the username: " + username + " already exists");
        }
        try {
            LogisticsOperator logisticsOperator = new LogisticsOperator(username, hasher.hash(password), name, email);
            entityManager.persist(logisticsOperator);
        } catch (ConstraintViolationException err) {
            throw new MyConstraintViolationException(err);
        }
    }

    public List<LogisticsOperator> getLogisticsOperators() {
        return entityManager.createNamedQuery("getLogisticsOperators", LogisticsOperator.class).getResultList();
    }

    public LogisticsOperator getPackages(String username) throws MyEntityNotFoundException {
        if(!this.exists(username)) {
            throw new MyEntityNotFoundException("The logistics operator with the username: " + username + " does not exist");
        }
        LogisticsOperator logisticsOperator = entityManager.find(LogisticsOperator.class, username);
        Hibernate.initialize(logisticsOperator.getPackages());
        return logisticsOperator;
    }

    public boolean exists(String username) {
        Query query = entityManager.createNamedQuery("logisticsOperatorsExists", LogisticsOperator.class);
        query.setParameter("username", username);
        return (Long) query.getSingleResult() > 0L;
    }

    public LogisticsOperator find(String username) throws MyEntityNotFoundException {
        LogisticsOperator logisticsOperator = entityManager.find(LogisticsOperator.class, username);
        if (logisticsOperator == null) {
            throw new MyEntityNotFoundException("The logistics operator with the username: " + username + " does not exist");
        }
        Hibernate.initialize(logisticsOperator.getPackages());
        return logisticsOperator;
    }

    public void update(String username, String name, String email) throws MyEntityNotFoundException {
        LogisticsOperator logisticsOperator = this.find(username);
        entityManager.lock(logisticsOperator, LockModeType.OPTIMISTIC);
        logisticsOperator.setName(name);
        logisticsOperator.setEmail(email);
    }

    public LogisticsOperator delete(String username) throws MyEntityNotFoundException {
        LogisticsOperator logisticsOperator = this.find(username);
        entityManager.remove(logisticsOperator);
        return logisticsOperator;
    }
}
