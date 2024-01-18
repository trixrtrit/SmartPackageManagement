package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.enums.PackageType;

import java.io.Serializable;

@Entity
@Table(name = "order_items",
        uniqueConstraints = @UniqueConstraint(columnNames = {"id", "product_id", "packageType"})
)
public class
OrderItem extends Versionable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int quantity;
    private int quantityLeft;
    private double price;

    @Enumerated(EnumType.STRING)
    private PackageType packageType;

    @ManyToOne
    @JoinColumn(name = "product_id")
    @NotNull
    private Product product;

    @ManyToOne
    @JoinColumn(name = "order_id")
    @NotNull
    private Order order;

    public OrderItem() {
    }

    public OrderItem(int quantity, double price, PackageType packageType, long productId) {
        this.quantity = quantity;
        this.price = price;
        //hack for order creation
        this.product = new Product();
        this.packageType = packageType;
        this.product.setId(productId);
    }

    public OrderItem(Long id, int quantity, int quantityLeft, double price, PackageType packageType, Product product, Order order) {
        this.id = id;
        this.quantity = quantity;
        this.quantityLeft = quantityLeft;
        this.price = price;
        this.packageType = packageType;
        this.product = product;
        this.order = order;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getQuantityLeft() {
        return quantityLeft;
    }

    public void setQuantityLeft(int quantityLeft) {
        this.quantityLeft = quantityLeft;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
    public PackageType getPackageType() {
        return packageType;
    }

    public void setPackageType(PackageType packageType) {
        this.packageType = packageType;
    }
}
