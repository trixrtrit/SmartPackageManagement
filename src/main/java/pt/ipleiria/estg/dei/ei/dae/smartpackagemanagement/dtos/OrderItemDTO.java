package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.dtos;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.Order;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.Product;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.Versionable;

public class
OrderItemDTO extends Versionable {

    private Long id;
    private float quantity;
    private double price;
    private Long orderId;
    private Long productId;
    private ProductDTO product;

    public OrderItemDTO() {
    }

    public OrderItemDTO(Long id, float quantity, double price, Long orderId, ProductDTO product) {
        this.id = id;
        this.quantity = quantity;
        this.price = price;
        this.orderId = orderId;
        this.product = product;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public float getQuantity() {
        return quantity;
    }

    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public ProductDTO getProduct() {
        return product;
    }

    public void setProduct(ProductDTO product) {
        this.product = product;
    }
}
