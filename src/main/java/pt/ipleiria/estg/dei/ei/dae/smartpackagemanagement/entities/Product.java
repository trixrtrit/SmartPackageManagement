package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "products",
        uniqueConstraints = @UniqueConstraint(columnNames = {"productReference", "manufacturer_username"})
)
@NamedQueries({
        @NamedQuery(
                name = "getProducts",
                query = "SELECT p FROM Product p ORDER BY p.name"
        )
})
public class Product extends Versionable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotNull
    private String name;
    @NotNull
    private String productReference;
    private String description;
    @Positive
    @NotNull
    private double price;
    @NotNull
    private boolean isActive;
    @PositiveOrZero
    private float unitStock;
    @PositiveOrZero
    private float boxStock;
    @PositiveOrZero
    private float containerStock;
    @ManyToOne
    @JoinColumn(name = "manufacturer_username")
    @NotNull
    private Manufacturer manufacturer;

    @ManyToOne
    @JoinColumn(name = "package_id")
    //@NotNull
    private Package aPackage;

    @OneToMany(mappedBy = "product", cascade = CascadeType.REMOVE)
    private List<OrderItem> orderItems;

    public Product() {
        this.orderItems = new ArrayList<OrderItem>();
    }

    public Product(
            String name,
            String description,
            double price,
            Manufacturer manufacturer,
            boolean isActive,
            String productReference
    ) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.manufacturer = manufacturer;
        this.isActive = isActive;
        this.productReference = productReference;
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

    public float getUnitStock() {
        return unitStock;
    }

    public void setUnitStock(float unitStock) {
        this.unitStock = unitStock;
    }

    public float getBoxStock() {
        return boxStock;
    }

    public void setBoxStock(float boxStock) {
        this.boxStock = boxStock;
    }

    public float getContainerStock() {
        return containerStock;
    }

    public void setContainerStock(float containerStock) {
        this.containerStock = containerStock;
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

    public String getProductReference() {
        return productReference;
    }

    public void setProductReference(String productReference) {
        this.productReference = productReference;
    }
}
