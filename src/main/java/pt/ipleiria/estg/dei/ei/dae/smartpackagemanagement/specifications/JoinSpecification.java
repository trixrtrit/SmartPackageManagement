package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.specifications;

import jakarta.persistence.criteria.*;

public class JoinSpecification<T, J> implements Specification<T> {
    private String joinField;
    private String fieldOfJoinedEntity;
    private Object value;
    private String operation;

    public JoinSpecification(String joinField, String fieldOfJoinedEntity, Object value, String operation) {
        this.joinField = joinField;
        this.fieldOfJoinedEntity = fieldOfJoinedEntity;
        this.value = value;
        this.operation = operation;
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaBuilder criteriaBuilder) {
        Join<T, J> join = root.join(joinField);
        Path<J> fieldPath = join.get(fieldOfJoinedEntity);

        if ("like".equalsIgnoreCase(operation)){
            return criteriaBuilder.like(fieldPath.as(String.class), "%" + value + "%");
        } else if ("notEqual".equalsIgnoreCase(operation)) {
            return criteriaBuilder.notEqual(fieldPath, value);
        } else { // Default to "equal"
            return criteriaBuilder.equal(fieldPath, value);
        }
    }
}
