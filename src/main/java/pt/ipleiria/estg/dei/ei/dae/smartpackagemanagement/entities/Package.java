package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.enums.PackageType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "packages")
@NamedQueries({
        @NamedQuery(
                name = "getPackages",
                query = "SELECT p FROM Package p ORDER BY p.packageType, p.material"
        ),
        @NamedQuery(
                name = "packageExists",
                query = "SELECT COUNT(p.code) FROM Package p WHERE p.code = :code"
        ),
        @NamedQuery(
                name = "findActivePackage",
                query = "SELECT p FROM Package p WHERE p.code = :code AND p.isActive"
        )
})
@SQLDelete(sql="UPDATE packages SET deleted = TRUE WHERE code = ? AND deleted = ?::boolean")
@Where(clause = "deleted IS FALSE")
public class Package extends Versionable {
    @Id
    private long code;
    private String material;
    @Enumerated(EnumType.STRING)
    private PackageType packageType;
    @OneToMany(mappedBy = "aPackage", cascade = CascadeType.REMOVE)
    private List<SensorPackage> sensorPackageList;
    @ManyToMany(mappedBy = "packages")
    private List<Product> products;

    @ManyToOne
    @JoinColumn(name = "stock_id")
    private Stock stock;

    @ManyToMany
    @JoinTable(
            name = "deliveries_packages",
            joinColumns = @JoinColumn(
                    name = "package_code",
                    referencedColumnName = "code"
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "delivery_id",
                    referencedColumnName = "id"
            )
    )
    private List<Delivery> deliveries;

    private boolean deleted;
    private boolean isActive = true;



    public Package() {
        this.sensorPackageList = new ArrayList<>();
        this.products = new ArrayList<Product>();
    }

    public Package(long code, String material, PackageType packageType) {
        this.code = code;
        this.material = material;
        this.packageType = packageType;
        this.sensorPackageList = new ArrayList<>();
        this.products = new ArrayList<Product>();
    }

    public long getCode() {
        return code;
    }

    public void setCode(long code) {
        this.code = code;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public PackageType getPackageType() {
        return packageType;
    }

    public void setPackageType(PackageType packageType) {
        this.packageType = packageType;
    }

    public List<SensorPackage> getSensorPackageList() {
        return sensorPackageList;
    }

    public void setSensorPackageList(List<SensorPackage> sensorPackageList) {
        this.sensorPackageList = sensorPackageList;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public Boolean getDeleted() {
        return deleted;
    }
    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public void addProduct(Product product) {
        if (!products.contains(product)) {
            products.add(product);
        }
    }
    public void removeProduct(Product product) {
        products.remove(product);
    }

    public List<Delivery> getDeliveries() {
        return deliveries;
    }

    public void addDelivery(Delivery delivery) {
        if (delivery != null && !deliveries.contains(delivery)){
            deliveries.add(delivery);
        }
    }

    public void removeDelivery(Delivery delivery) {
        if (delivery != null && deliveries.contains(delivery)){
            deliveries.remove(delivery);
        }
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
