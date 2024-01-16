package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.validation.ConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.Order;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.OrderLog;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.enums.OrderStatus;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyEntityNotFoundException;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class OrderLogBean {

    @PersistenceContext
    private EntityManager entityManager;

    public long create(String logEntry, long orderId)
            throws MyConstraintViolationException, MyEntityNotFoundException {
        Order order = entityManager.find(Order.class, orderId);
        if (order == null) {
            throw new MyEntityNotFoundException("No order with id '" + orderId + "' has been found.");
        }
        try {
            var newEntry = new OrderLog(logEntry, order);
            newEntry.setTimestamp(Instant.now());

            entityManager.persist(newEntry);
            return newEntry.getId();
        } catch (ConstraintViolationException err) {
            throw new MyConstraintViolationException(err);
        }
    }

    public OrderLog find(long orderLogId)  throws MyEntityNotFoundException {
        OrderLog orderLog = entityManager.find(OrderLog.class, orderLogId);
        if (orderLog == null) {
            throw new MyEntityNotFoundException("The orderLog with the id: " + orderLogId + " does not exist");
        }
        return orderLog;
    }

    public List<OrderLog> getOrderLogs(
            Long orderId,
            Instant startDate,
            Instant endDate,
            OrderStatus status
    ) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<OrderLog> query = builder.createQuery(OrderLog.class);
        Root<OrderLog> root = query.from(OrderLog.class);

        List<Predicate> predicates = new ArrayList<>();

        if (orderId != null) {
            predicates.add(builder.equal(root.get("order").get("id"), orderId));
        }

        //todo ver se instant e date podem ser "interchangable"
        if (startDate != null && endDate != null) {
            predicates.add(builder.between(root.get("order").get("date"), startDate, endDate));
        } else if (startDate != null) {
            predicates.add(builder.greaterThanOrEqualTo(root.get("order").get("date"), startDate));
        } else if (endDate != null) {
            predicates.add(builder.lessThanOrEqualTo(root.get("order").get("date"), endDate));
        }

        if(status != null) {
            predicates.add(builder.isNull(root.get("order").get("status")));
        }

        query.where(builder.and(predicates.toArray(new Predicate[0])));

        query.orderBy(builder.asc(root.get("order").get("id")),
                builder.asc(root.get("order").get("date")),
                builder.asc(root.get("order").get("status")));

        return entityManager.createQuery(query).getResultList();
    }

}