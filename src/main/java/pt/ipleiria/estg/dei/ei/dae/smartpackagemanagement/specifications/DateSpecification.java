package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.specifications;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.time.Instant;

public class DateSpecification<T> implements Specification<T> {
    private final String field;
    private final Instant value;
    private final String operation;

    public DateSpecification(String field, Instant value, String operation) {
        this.field = field;
        this.value = value;
        this.operation = operation;
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaBuilder criteriaBuilder) {
        if("gte".equals(operation)){
            return criteriaBuilder.greaterThanOrEqualTo(root.get(field), value);
        } else if ("lte".equals(operation)) {
            return criteriaBuilder.lessThanOrEqualTo(root.get(field), value);
        } else if ("eq".equals(operation)) {
            return criteriaBuilder.equal(root.get(field), value);
        }
        return null;
    }
}
