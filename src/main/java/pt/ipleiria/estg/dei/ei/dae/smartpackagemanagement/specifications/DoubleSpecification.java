package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.specifications;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class DoubleSpecification<T> implements Specification<T>{
    private final String field;
    private final Double value;
    private final String operation;

    public DoubleSpecification(String field, Double value, String operation) {
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
