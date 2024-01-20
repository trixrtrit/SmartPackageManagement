package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import org.jboss.resteasy.util.DateUtil;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.specifications.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Stateless
public class QueryBean<T> {
    @PersistenceContext
    private EntityManager entityManager;
    private static final Logger logger = Logger.getLogger("ebjs.QueryBean");

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
                    if (parts.length > 3) {
                        String joinField = parts[1];
                        String fieldOfJoinedEntity = parts[2];
                        String operation = parts[3];
                        Boolean isManyToMany = false;

                        if (parts.length > 4) {
                            System.out.println("XD Is Many to Many");
                            isManyToMany = true;
                        }

                        specifications.add(new JoinSpecification<>(
                                joinField, fieldOfJoinedEntity, fieldValue, operation, isManyToMany));
                    }
                } else {
                    String operation = "";
                    if (keyParts.length > 2) {
                        operation += keyParts[2];
                    }

                    if (dataType.contains("Enum")) {
                        var dataTypeParts = dataType.split(GenericFilterMapBuilder.getEnumSeparator());
                        var enumName = dataTypeParts[1];
                        specifications.add(new EnumSpecification<T>(enumName, fieldName, fieldValue, operation));
                    } else {
                        switch (dataType) {
                            case "Double":
                                specifications.add(new DoubleSpecification<T>(fieldName, Double.parseDouble(fieldValue), operation));
                                break;
                            case "Boolean":
                                specifications.add(new BooleanSpecification<T>(fieldName, Boolean.parseBoolean(fieldValue)));
                                break;
                            case "Date":
                                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                try {
                                    specifications.add(new DateSpecification<T>(fieldName, formatter.parse(fieldValue), operation));
                                } catch (DateUtil.DateParseException | ParseException ex) {
                                    logger.warning(ex.getMessage());
                                }
                                break;
                            case "Long":
                                specifications.add(new CodeSpecification<>(fieldName, Long.parseLong(fieldValue), operation));
                                break;
                            case "ManufacturerPackage":
                                specifications.add(new ManufacturerProductSpecification<>(fieldValue));
                                break;
                            case "CustomerPackage":
                                specifications.add(new CustomerPackageSpecification<>(fieldValue));
                                break;
                            default:
                                specifications.add(new DefaultStringSpecification<T>(fieldName, fieldValue));
                        }
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
