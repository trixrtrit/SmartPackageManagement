package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.specifications;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.*;

public class CustomerPackageSpecification <StandardPackage> implements Specification<StandardPackage> {
    private final String customerUsername;

    public CustomerPackageSpecification(String customerUsername) {
        this.customerUsername = customerUsername;
    }

    @Override
    public Predicate toPredicate(Root<StandardPackage> root, CriteriaBuilder criteriaBuilder) {
        Join<StandardPackage, Delivery> deliveryJoin = root.join("deliveries");
        Join<Delivery, Order> orderJoin = deliveryJoin.join("order");
        Join<Order, Customer> customerJoin = orderJoin.join("customer");

        return criteriaBuilder.equal(customerJoin.get("username"), this.customerUsername);
    }
}
