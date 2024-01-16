package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.ejbs;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.validation.ConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.LogisticsOperator;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.security.Hasher;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Stateless
public class LogisticsOperatorBean {
    @PersistenceContext
    private EntityManager entityManager;
    @Inject
    private Hasher hasher;
    @EJB
    private QueryBean<LogisticsOperator> logisticsOperatorQueryBean;

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

    public List<LogisticsOperator> getLogisticsOperators(Map<String, String> filterMap, int pageNumber, int pageSize)
            throws IllegalArgumentException {
        Map<String, String> orderMap = new LinkedHashMap<>();
        orderMap.put("name", "asc");
        orderMap.put("username", "asc");
        return logisticsOperatorQueryBean.getEntities(LogisticsOperator.class, filterMap, orderMap, pageNumber, pageSize);
    }

    public long getLogisticsOperatorsCount(Map<String, String> filterMap) {
        return logisticsOperatorQueryBean.getEntitiesCount(LogisticsOperator.class, filterMap);
    }

    public boolean exists(String username) {
        Query query = entityManager.createNamedQuery("logisticsOperatorExists", LogisticsOperator.class);
        query.setParameter("username", username);
        return (Long) query.getSingleResult() > 0L;
    }

    public LogisticsOperator find(String username) throws MyEntityNotFoundException {
        LogisticsOperator logisticsOperator = entityManager.find(LogisticsOperator.class, username);
        if (logisticsOperator == null) {
            throw new MyEntityNotFoundException("The logistics operator with the username: " + username + " does not exist");
        }
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
