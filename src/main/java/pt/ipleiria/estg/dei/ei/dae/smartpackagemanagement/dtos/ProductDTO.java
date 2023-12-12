package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.dtos;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.OrderItem;

import java.util.ArrayList;
import java.util.List;

public class ProductDTO {

    private Long id;
    private String name;
    private String description;
    @Positive
    private double price;
    private boolean isActive;
    @PositiveOrZero
    private float stock;
    private String manufacturerUsername;
    private long packageId;

    //TODO: OrderItemDTO
    //private List<OrderItemDTO> orderItems;

    public ProductDTO() {
        //this.orderItems = new ArrayList<>();
    }

    public ProductDTO(
            Long id,
            String name,
            String description,
            double price,
            boolean isActive,
            float stock,
            String manufacturerUsername,
            long packageId
    ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.isActive = isActive;
        this.stock = stock;
        this.manufacturerUsername = manufacturerUsername;
        this.packageId = packageId;
        //this.orderItems = new ArrayList<>();
    }

    /*
    public ProductDTO(Long id, String name, String description, double price, boolean isActive, float stock, List<OrderItem> orderItems) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.isActive = isActive;
        this.stock = stock;
        this.orderItems = orderItems;
    }*/

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public String getManufacturerUsername() {
        return manufacturerUsername;
    }

    public void setManufacturerUsername(String manufacturerUsername) {
        this.manufacturerUsername = manufacturerUsername;
    }

    public long getPackageId() {
        return packageId;
    }

    public void setPackageId(long packageId) {
        this.packageId = packageId;
    }

    /*
    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }*/
}
