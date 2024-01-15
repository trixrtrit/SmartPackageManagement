package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Stateless
public class QueryBean<T> {
    @PersistenceContext
    private EntityManager entityManager;

    public List<T> getEntities(Class<T> entity, Map<String, String> filterMap, int pageNumber, int pageSize) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> query = builder.createQuery(entity);
        Root<T> root = query.from(entity);
        List<Predicate> predicates = getPredicates(filterMap, builder, root);

        query.where(builder.and(predicates.toArray(new Predicate[0])));

        query.orderBy(builder.asc(root.get("name")), builder.asc(root.get("username")));

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

    private List<Predicate> getPredicates(
            Map<String, String> filterMap,
            CriteriaBuilder builder,
            Root<T> root
    ) {
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
}
