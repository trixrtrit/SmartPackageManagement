package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities;

import jakarta.persistence.*;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.enums.PackageType;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@NamedQueries({
        @NamedQuery(
                name = "standardPackageExists",
                query = "SELECT COUNT(p.code) FROM StandardPackage p WHERE p.code = :code"
        )
})
@SQLDelete(sql="UPDATE packages SET deleted = TRUE WHERE code = ? AND deleted = ?::boolean")
@Where(clause = "deleted IS FALSE")
public class StandardPackage extends Package {

    @Enumerated(EnumType.STRING)
    private PackageType packageType;
    @ManyToMany(mappedBy = "standardPackages")
    private List<Product> products;

    @OneToMany(mappedBy = "standardPackage", cascade = CascadeType.REMOVE)
    private List<TransportPackageStandardPackage> transportPackageStandardPackages;
    private Long initialProductId;

    public StandardPackage() {
        this.products = new ArrayList<Product>();
        this.transportPackageStandardPackages = new ArrayList<TransportPackageStandardPackage>();
    }

    public StandardPackage(long code, String material, PackageType packageType, Date manufactureDate) {
        super(code, material, manufactureDate);
        this.packageType = packageType;
        this.products = new ArrayList<Product>();
        this.transportPackageStandardPackages = new ArrayList<TransportPackageStandardPackage>();
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

    public List<TransportPackageStandardPackage> getTransportPackageStandardPackages() {
        return transportPackageStandardPackages;
    }

    public void setTransportPackageStandardPackages(List<TransportPackageStandardPackage> transportPackageStandardPackages) {
        this.transportPackageStandardPackages = transportPackageStandardPackages;
    }

    public PackageType getPackageType() {
        return packageType;
    }

    public void setPackageType(PackageType packageType) {
        this.packageType = packageType;
    }

    public Long getInitialProductId() {
        return initialProductId;
    }

    public void setInitialProductId(Long initialProductId) {
        this.initialProductId = initialProductId;
    }
}
