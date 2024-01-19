package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.specifications;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class DefaultStringSpecification<T> implements Specification<T> {
    private final String field;
    private final String pattern;

    public DefaultStringSpecification(String field, String pattern) {
        this.field = field;
        this.pattern = pattern;
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaBuilder criteriaBuilder) {

        String[] path = field.split("\\.");
        Path<String> fieldPath = root.get(path[0]);
        for (int i = 1; i < path.length; i++) {
            fieldPath = fieldPath.get(path[i]);
        }
        return criteriaBuilder.like(criteriaBuilder.lower(fieldPath), "%" + pattern.toLowerCase() + "%");
    }
}
