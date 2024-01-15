package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Stateless
public class QueryBean<T> {
    @PersistenceContext
    private EntityManager entityManager;

    public List<T> getEntities(
            Class<T> entity,
            Map<String, String> filterMap,
            Map<String, String> orderMap,
            int pageNumber,
            int pageSize
    )  throws IllegalArgumentException
    {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> query = builder.createQuery(entity);
        Root<T> root = query.from(entity);
        List<Predicate> predicates = getPredicates(filterMap, builder, root);

        query.where(builder.and(predicates.toArray(new Predicate[0])));
        List<Order> orderList = getOrderBy(orderMap, builder, root);
        query.orderBy(orderList);

        return entityManager.createQuery(query).
                setFirstResult((pageNumber - 1) * pageSize).
                setMaxResults(pageSize).getResultList();
    }

    public long getEntitiesCount(Class<T> entity, Map<String, String> filterMap) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = builder.createQuery(Long.class);
        Root<T> root = query.from(entity);
        List<Predicate> predicates = getPredicates(filterMap, builder, root);

        query.select(builder.count(root));
        query.where(builder.and(predicates.toArray(new Predicate[0])));

        return entityManager.createQuery(query).getSingleResult();
    }

    private List<Predicate> getPredicates(Map<String, String> filterMap, CriteriaBuilder builder, Root<T> root) {
        List<Predicate> predicates = new ArrayList<>();
        for (Map.Entry<String, String> entry : filterMap.entrySet()) {
            String fieldName = entry.getKey();
            String fieldValue = entry.getValue();
            if (fieldValue != null) {
                predicates.add(builder.like(builder.lower(root.get(fieldName)), "%" + fieldValue.toLowerCase() + "%"));
            }
        }
        return predicates;
    }

    private List<Order> getOrderBy(Map<String, String> orderMap, CriteriaBuilder builder, Root<T> root)
        throws IllegalArgumentException {
        Order order;
        List<Order> orderList = new ArrayList<>();
        for (Map.Entry<String, String> entry : orderMap.entrySet()) {
            String fieldName = entry.getKey();
            String sortOrder = entry.getValue();
            if ("asc".equalsIgnoreCase(sortOrder)) {
                order = builder.asc(root.get(fieldName));
            } else if ("desc".equalsIgnoreCase(sortOrder)) {
                order = builder.desc(root.get(fieldName));
            } else {
                throw new IllegalArgumentException("Invalid sort order: " + sortOrder);
            }
            orderList.add(order);
        }
        return orderList;
    }
}
