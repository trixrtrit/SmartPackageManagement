package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "products")
public class Product extends Versionable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private double price;
    private boolean isActive;
    private float stock;
    @ManyToOne
    @JoinColumn(name = "manufacturer_username")
    @NotNull
    private Manufacturer manufacturer;

    @ManyToOne
    @JoinColumn(name = "package_id")
    @NotNull
    private Package aPackage;

    @OneToMany(mappedBy = "product", cascade = CascadeType.REMOVE)
    private List<OrderItem> orderItems;

    public Product() {
        this.orderItems = new ArrayList<OrderItem>();
    }

    public Product(long id, String name, String description, double price, Manufacturer manufacturer, Package aPackage, boolean isActive, float stock) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.manufacturer = manufacturer;
        this.aPackage = aPackage;
        this.isActive = isActive;
        this.stock = stock;
        this.orderItems = new ArrayList<OrderItem>();
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public float getStock() {
        return stock;
    }

    public void setStock(float stock) {
        this.stock = stock;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Manufacturer getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(Manufacturer manufacturer) {
        this.manufacturer = manufacturer;
    }

    public Package getaPackage() {
        return aPackage;
    }

    public void setaPackage(Package aPackage) {
        this.aPackage = aPackage;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }
}
