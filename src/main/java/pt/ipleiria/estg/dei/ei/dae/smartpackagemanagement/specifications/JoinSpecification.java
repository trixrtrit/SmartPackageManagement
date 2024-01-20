package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.specifications;

import jakarta.persistence.criteria.*;

import java.util.Collection;

public class JoinSpecification<T, J> implements Specification<T> {
    private final Boolean isManyToMany;
    private String joinField;
    private String fieldOfJoinedEntity;
    private Object value;
    private String operation;

    public JoinSpecification(String joinField, String fieldOfJoinedEntity, Object value, String operatio, Boolean isManyToMany) {
        this.joinField = joinField;
        this.fieldOfJoinedEntity = fieldOfJoinedEntity;
        this.value = value;
        this.operation = operation;
        this.isManyToMany = isManyToMany;
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaBuilder criteriaBuilder) {
        if (isManyToMany) {
            Join<T, Collection<J>> join = root.joinCollection(joinField);
            Expression<J> fieldPath = join.get(fieldOfJoinedEntity);

            return getPredicate(criteriaBuilder, fieldPath);
        } else {
            Join<T, J> join = root.join(joinField);
            Path<J> fieldPath = join.get(fieldOfJoinedEntity);

            return getPredicate(criteriaBuilder, fieldPath);
        }
    }

    private Predicate getPredicate(CriteriaBuilder criteriaBuilder, Expression<J> fieldPath) {
        if ("like".equalsIgnoreCase(operation)){
            return criteriaBuilder.like(fieldPath.as(String.class), "%" + value + "%");
        } else if ("notEqual".equalsIgnoreCase(operation)) {
            return criteriaBuilder.notEqual(fieldPath, value);
        } else { // Default to "equal"
            return criteriaBuilder.equal(fieldPath, value);
        }
    }
}
