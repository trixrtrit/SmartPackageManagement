package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "packages")
@NamedQueries({
        @NamedQuery(
                name = "packageExists",
                query = "SELECT COUNT(p.code) FROM Package p WHERE p.code = :code"
        ),
        @NamedQuery(
                name = "findActivePackage",
                query = "SELECT p FROM Package p WHERE p.code = :code AND p.isActive"
        ),
        @NamedQuery(
                name = "findPackageForCustomer",
                query = "SELECT COUNT(p.code) FROM Package p " +
                        "JOIN p.deliveries d " +
                        "WHERE d.order.customer.username = :username " +
                        "AND p.code = :code"
        ),
        @NamedQuery(
                name = "findCustomerPackage",
                query = "SELECT d.order.customer FROM Package p " +
                        "JOIN p.deliveries d " +
                        "WHERE p.code = :code AND d.status = :status"
        ),
})
@SQLDelete(sql="UPDATE packages SET deleted = TRUE WHERE code = ? AND deleted = ?::boolean")
@Where(clause = "deleted IS FALSE")
public class Package extends Versionable{
    @Id
    private long code;
    private String material;
    @OneToMany(mappedBy = "aPackage", cascade = CascadeType.REMOVE)
    private List<SensorPackage> sensorPackageList;

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
    @Temporal(TemporalType.TIMESTAMP)
    private Date manufactureDate;

    public Package() {
        this.sensorPackageList = new ArrayList<>();
    }

    public Package(long code, String material) {
        this.code = code;
        this.material = material;
        this.sensorPackageList = new ArrayList<>();
        this.manufactureDate = new Date();
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

    public Boolean getDeleted() {
        return deleted;
    }
    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
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

    public Date getManufactureDate() {
        return manufactureDate;
    }

    public void setManufactureDate(Date manufactureDate) {
        this.manufactureDate = manufactureDate;
    }
}
