package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities;

import jakarta.persistence.Entity;

import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.enums.PackageType;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "standard_package")
@SQLDelete(sql="UPDATE packages SET deleted = TRUE WHERE code = ? AND deleted = ?::boolean")
@Where(clause = "deleted IS FALSE")
public class StandardPackage extends Package {

    @ManyToMany(mappedBy = "standardPackages")
    private List<Product> products;

    public StandardPackage() {
        this.products = new ArrayList<Product>();
    }

    public StandardPackage(long code, String material, PackageType packageType) {
        super(code, material, packageType);
        this.products = new ArrayList<Product>();
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public void addProduct(Product product) {
        if (!products.contains(product)) {
            products.add(product);
        }
    }
    public void removeProduct(Product product) {
        products.remove(product);
    }
}
