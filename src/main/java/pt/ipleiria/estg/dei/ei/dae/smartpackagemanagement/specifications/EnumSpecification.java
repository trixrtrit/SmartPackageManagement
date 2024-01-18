package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.specifications;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.time.Instant;

import static pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.specifications.PredicateUtils.handleEnumPredicate;

public class EnumSpecification<T> implements Specification<T> {
    private final String enumName;
    private final String field;
    private final String value;
    private final String operation;


    public EnumSpecification(String enumName, String field, String value, String operation) {
        this.enumName = enumName;
        this.field = field;
        this.value = value;
        this.operation = operation;
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaBuilder criteriaBuilder) {
        return handleEnumPredicate(enumName, field, value, operation, criteriaBuilder, root);
    }
}
