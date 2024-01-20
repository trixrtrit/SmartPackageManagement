package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.specifications;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.Manufacturer;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.Product;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.StandardPackageProduct;

public class ManufacturerProductSpecification<StandardPackage> implements Specification<StandardPackage> {
    private final String manufacturerUsername;

    public ManufacturerProductSpecification(String manufacturerUsername) {
        this.manufacturerUsername = manufacturerUsername;
    }

    @Override
    public Predicate toPredicate(Root<StandardPackage> root, CriteriaBuilder criteriaBuilder) {
        Join<StandardPackage, StandardPackageProduct> standardPackageProductJoin = root.join("standardPackageProducts");
        Join<StandardPackageProduct, Product> productJoin = standardPackageProductJoin.join("product");
        Join<Product, Manufacturer> manufacturerJoin = productJoin.join("manufacturer");

        return criteriaBuilder.equal(manufacturerJoin.get("username"), this.manufacturerUsername);
    }
}
