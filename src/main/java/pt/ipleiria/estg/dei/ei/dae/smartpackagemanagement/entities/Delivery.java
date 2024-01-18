package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.enums.DeliveryStatus;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "deliveries")
@NamedQueries({
        @NamedQuery(
                name = "getDeliveries",
                query = "SELECT d FROM Delivery d ORDER BY d.dispatchedDate DESC"
        ),
        @NamedQuery(
                name = "getOrderDeliveries",
                query = "SELECT d FROM Delivery d WHERE d.order.id = :orderId ORDER BY d.dispatchedDate DESC"
        )
})
public class Delivery extends Versionable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Instant dispatchedDate;
    @Null
    private Instant deliveredDate;
    @Enumerated(EnumType.STRING)
    private DeliveryStatus status;

    @ManyToOne
    @JoinColumn(name = "order_id")
    @NotNull
    private Order order;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "deliveries_packages",
            joinColumns = @JoinColumn(
                    name = "delivery_id",
                    referencedColumnName = "id"
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "package_code",
                    referencedColumnName = "code"
            )
    )
    private List<Package> packages;

    public Delivery() {
        this.packages = new ArrayList<>();
    }

    public Delivery(Instant dispatchedDate, DeliveryStatus status, Order order) {
        this.dispatchedDate = dispatchedDate;
        this.status = status;
        this.order = order;
        this.packages = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDispatchedDate() {
        return dispatchedDate;
    }

    public void setDispatchedDate(Instant dispatchedDate) {
        this.dispatchedDate = dispatchedDate;
    }

    public Instant getDeliveredDate() {
        return deliveredDate;
    }

    public void setDeliveredDate(Instant deliveredDate) {
        this.deliveredDate = deliveredDate;
    }

    public DeliveryStatus getStatus() {
        return status;
    }

    public void setStatus(DeliveryStatus status) {
        this.status = status;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public List<Package> getPackages() {
        return packages;
    }

    public void setPackages(List<Package> packages) {
        this.packages = packages;
    }

    public void addPackage(Package aPackage){
        if (aPackage != null && !packages.contains(aPackage)){
            packages.add(aPackage);
        }
    }

    public void removePackage(Package aPackage){
        if (aPackage != null && packages.contains(aPackage)){
            packages.add(aPackage);
        }
    }
}
