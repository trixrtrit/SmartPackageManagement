package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.Date;

@Entity
@Table(name = "standard_package_product")
@NamedQueries({
        @NamedQuery(
                name = "standardPackageProductExists",
                query = "SELECT COUNT(sp.id) FROM StandardPackageProduct sp " +
                        "WHERE sp.standardPackage.code = :standardPkgCode AND sp.product.id = :productId " +
                        "AND sp.removedAt IS NULL"
        ),
        @NamedQuery(
                name = "findStandardPackageProduct",
                query = "SELECT sp FROM StandardPackageProduct sp " +
                        "WHERE sp.standardPackage.code = :standardPkgCode AND sp.product.id = :productId " +
                        "AND sp.removedAt IS NULL"
        ),
        @NamedQuery(
                name = "findAllStandardPackagesForProducts",
                query = "SELECT sp FROM StandardPackageProduct sp " +
                        "WHERE sp.product.id = :productId "
        ),
})
public class StandardPackageProduct extends Versionable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "standard_package_id")
    private StandardPackage standardPackage;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "product_id")
    private Product product;

    @Temporal(TemporalType.TIMESTAMP)
    private Date addedAt;
    @Temporal(TemporalType.TIMESTAMP)
    private Date removedAt;

    public StandardPackageProduct() {
    }

    public StandardPackageProduct(StandardPackage standardPackage, Product product) {
        this.standardPackage = standardPackage;
        this.product = product;
        addedAt = new Date();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public StandardPackage getStandardPackage() {
        return standardPackage;
    }

    public void setStandardPackage(StandardPackage standardPackage) {
        this.standardPackage = standardPackage;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Date getAddedAt() {
        return addedAt;
    }

    public void setAddedAt(Date addedAt) {
        this.addedAt = addedAt;
    }

    public Date getRemovedAt() {
        return removedAt;
    }

    public void setRemovedAt(Date removedAt) {
        this.removedAt = removedAt;
    }
}
