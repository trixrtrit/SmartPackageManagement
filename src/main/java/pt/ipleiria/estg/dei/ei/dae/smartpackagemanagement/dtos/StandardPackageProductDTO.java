package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.dtos;

import java.io.Serializable;
import java.util.Date;

public class StandardPackageProductDTO implements Serializable {
    private long id;
    private StandardPackageDTO standardPackage;
    private ProductDTO product;
    private Date addedAt;
    private Date removedAt;

    public StandardPackageProductDTO() {
    }

    public StandardPackageProductDTO(long id, StandardPackageDTO standardPackage, ProductDTO product, Date addedAt, Date removedAt) {
        this.id = id;
        this.standardPackage = standardPackage;
        this.product = product;
        this.addedAt = addedAt;
        this.removedAt = removedAt;
    }

    public StandardPackageProductDTO(long id, StandardPackageDTO standardPackage, Date addedAt, Date removedAt) {
        this.id = id;
        this.standardPackage = standardPackage;
        this.addedAt = addedAt;
        this.removedAt = removedAt;
    }

    public StandardPackageProductDTO(long id, ProductDTO product, Date addedAt, Date removedAt) {
        this.id = id;
        this.product = product;
        this.addedAt = addedAt;
        this.removedAt = removedAt;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public StandardPackageDTO getStandardPackage() {
        return standardPackage;
    }

    public void setStandardPackage(StandardPackageDTO standardPackage) {
        this.standardPackage = standardPackage;
    }

    public ProductDTO getProduct() {
        return product;
    }

    public void setProduct(ProductDTO product) {
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
