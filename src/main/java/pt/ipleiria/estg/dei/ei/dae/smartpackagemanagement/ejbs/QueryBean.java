package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.enums.PackageType;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.specifications.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    ) throws IllegalArgumentException {
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

    private List<Predicate> getPredicates(
            Map<String, String> filterMap,
            CriteriaBuilder builder,
            Root<T> root
    ) {
        List<Specification<T>> specifications = new ArrayList<>();
        String separator = GenericFilterMapBuilder.getSeparator();
        for (Map.Entry<String, String> entry : filterMap.entrySet()) {
            String keyName = entry.getKey();
            String fieldValue = entry.getValue();
            if (fieldValue != null) {
                String[] keyParts = keyName.split(separator);
                String dataType = keyParts[0];
                String fieldName = keyParts[1];
                if (dataType.equals("Join")) {
                    // Assuming format: "Join/joinField/fieldOfJoinedEntity/operation"
                    String[] parts = keyName.split(separator);
                    if (parts.length >= 4) {
                        String joinField = parts[1];
                        String fieldOfJoinedEntity = parts[2];
                        String operation = parts[3];

                        specifications.add(new JoinSpecification<>(
                                joinField, fieldOfJoinedEntity, fieldValue, operation));
                    }
                } else {
                    String operation = "";
                    if (keyParts.length > 2) {
                        operation += keyParts[2];
                    }
                    switch (dataType) {
                        case "Enum":
                            specifications.add(new EnumSpecification<T>(fieldName, fieldValue, operation));
                            break;
                        case "Double":
                            specifications.add(new DoubleSpecification<T>(fieldName, Double.parseDouble(fieldValue), operation));
                            break;
                        case "Boolean":
                            specifications.add(new BooleanSpecification<T>(fieldName, Boolean.parseBoolean(fieldValue)));
                            break;
                        case "Instant":
                            specifications.add(new DateSpecification<T>(fieldName, Instant.parse(fieldValue), operation));
                            break;
                        case "Long":
                            specifications.add(new CodeSpecification<>(fieldName, Long.parseLong(fieldValue), operation));
                            break;
                        default:
                            if (operation.equals("enum")) {
                                specifications.add(new PackageTypeSpecification<T>(fieldName, PackageType.valueOf(fieldValue)));
                            } else
                                specifications.add(new DefaultStringSpecification<T>(fieldName, fieldValue));
                            break;
                    }
                }
            }
        }
        return buildPredicates(specifications, root, builder);
    }

    public List<Predicate> buildPredicates
            (List<Specification<T>> specifications, Root<T> root, CriteriaBuilder criteriaBuilder) {
        return specifications.stream()
                .map(spec -> spec.toPredicate(root, criteriaBuilder))
                .collect(Collectors.toList());
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
