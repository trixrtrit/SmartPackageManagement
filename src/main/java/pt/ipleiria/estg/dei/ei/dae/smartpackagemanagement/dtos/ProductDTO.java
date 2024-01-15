package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.dtos;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.ProductParameter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ProductDTO implements Serializable {

    private Long id;
    private String name;
    private String description;
    @Positive
    private double price;
    private boolean isActive;
    private String manufacturerUsername;
    private String reference;
    @PositiveOrZero
    private float unitStock;
    @PositiveOrZero
    private float boxStock;
    @PositiveOrZero
    private float containerStock;

    @PositiveOrZero
    private int primaryPackQuantity;
    @PositiveOrZero
    private int secondaryPackQuantity;
    @PositiveOrZero
    private int tertiaryPackQuantity;

    //TODO: OrderItemDTO
    //private List<OrderItemDTO> orderItems;

    private List<ProductParameter> productParameters;

    public ProductDTO() {
        //this.orderItems = new ArrayList<>();
        this.productParameters = new ArrayList<>();
    }

    public ProductDTO(
            Long id,
            String name,
            String description,
            double price,
            boolean isActive,
            String manufacturerUsername,
            String reference,
            float unitStock,
            float boxStock,
            float containerStock,
            int primaryPackQuantity,
            int secondaryPackQuantity,
            int tertiaryPackQuantity
    ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.isActive = isActive;
        this.reference = reference;
        this.manufacturerUsername = manufacturerUsername;
        this.unitStock = unitStock;
        this.boxStock = boxStock;
        this.containerStock = containerStock;
        this.productParameters = new ArrayList<>();
        this.primaryPackQuantity = primaryPackQuantity;
        this.secondaryPackQuantity = secondaryPackQuantity;
        this.tertiaryPackQuantity = tertiaryPackQuantity;
        //this.orderItems = new ArrayList<>();
    }

    public ProductDTO(
            Long id,
            String name,
            String description,
            double price,
            boolean isActive,
            String manufacturerUsername,
            String reference
    ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.isActive = isActive;
        this.reference = reference;
        this.manufacturerUsername = manufacturerUsername;
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

    public String getManufacturerUsername() {
        return manufacturerUsername;
    }

    public void setManufacturerUsername(String manufacturerUsername) {
        this.manufacturerUsername = manufacturerUsername;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
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

    public int getPrimaryPackQuantity() {
        return primaryPackQuantity;
    }

    public void setPrimaryPackQuantity(int primaryPackQuantity) {
        this.primaryPackQuantity = primaryPackQuantity;
    }

    public int getSecondaryPackQuantity() {
        return secondaryPackQuantity;
    }

    public void setSecondaryPackQuantity(int secondaryPackQuantity) {
        this.secondaryPackQuantity = secondaryPackQuantity;
    }

    public int getTertiaryPackQuantity() {
        return tertiaryPackQuantity;
    }

    public void setTertiaryPackQuantity(int tertiaryPackQuantity) {
        this.tertiaryPackQuantity = tertiaryPackQuantity;
    }

    /*
    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }*/
}
