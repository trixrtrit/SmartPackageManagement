package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.specifications;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;



public interface Specification<T> {
    Predicate toPredicate(Root<T> root, CriteriaBuilder criteriaBuilder);
}
