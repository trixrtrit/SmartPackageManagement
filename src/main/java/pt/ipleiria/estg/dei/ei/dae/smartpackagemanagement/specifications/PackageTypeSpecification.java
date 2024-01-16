package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.specifications;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.enums.PackageType;

public class PackageTypeSpecification<T> implements Specification<T> {
    private final String field;
    private final PackageType value;

    public PackageTypeSpecification(String field, PackageType value) {
        this.field = field;
        this.value = value;
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaBuilder criteriaBuilder) {
        return criteriaBuilder.equal(root.get(field), value);
    }
}
