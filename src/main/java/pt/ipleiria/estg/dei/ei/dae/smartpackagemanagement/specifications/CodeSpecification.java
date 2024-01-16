package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.specifications;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class CodeSpecification<T> implements Specification<T>{
    private final String field;
    private final Long value;
    private final String operation;

    public CodeSpecification(String field, Long value, String operation) {
        this.field = field;
        this.value = value;
        this.operation = operation;
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaBuilder criteriaBuilder) {
        return criteriaBuilder.equal(root.get(field), value);
    }
}
