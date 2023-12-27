package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.enums.PackageType;

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
        )
})
@SQLDelete(sql="UPDATE packages SET deleted = TRUE, isactive = FALSE WHERE code = ? AND deleted = ?::boolean")
@Where(clause = "deleted IS FALSE")
public class Package extends Versionable{
    @Id
    private long code;
    private String material;
    @Enumerated(EnumType.STRING)
    private PackageType packageType;
    @ManyToMany(mappedBy = "packages")
    private List<Sensor> sensors;
    @ManyToMany(mappedBy = "packages")
    private List<Product> products;
    private boolean deleted;

    public Package() {
        this.sensors = new ArrayList<Sensor>();
        this.products = new ArrayList<Product>();
    }

    public Package(long code, String material, PackageType packageType) {
        this.code = code;
        this.material = material;
        this.packageType = packageType;
        this.sensors = new ArrayList<Sensor>();
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

    public List<Sensor> getSensors() {
        return sensors;
    }

    public void setSensors(List<Sensor> sensors) {
        this.sensors = sensors;
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

    public void addSensor(Sensor sensor) {
        if (!sensors.contains(sensor)) {
            sensors.add(sensor);
        }
    }
    public void removeSensor(Sensor sensor) {
        sensors.remove(sensor);
    }
}
