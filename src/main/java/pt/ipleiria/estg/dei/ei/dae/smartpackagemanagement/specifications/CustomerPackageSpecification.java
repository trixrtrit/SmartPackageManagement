package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.specifications;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.*;

public class CustomerPackageSpecification <Package> implements Specification<Package> {
    private final String customerUsername;

    public CustomerPackageSpecification(String customerUsername) {
        this.customerUsername = customerUsername;
    }

    @Override
    public Predicate toPredicate(Root<Package> root, CriteriaBuilder criteriaBuilder) {
        Join<Package, Delivery> deliveryJoin = root.join("deliveries");
        Join<Delivery, Order> orderJoin = deliveryJoin.join("order");
        Join<Order, Customer> customerJoin = orderJoin.join("customer");

        return criteriaBuilder.equal(customerJoin.get("username"), this.customerUsername);
    }
}
