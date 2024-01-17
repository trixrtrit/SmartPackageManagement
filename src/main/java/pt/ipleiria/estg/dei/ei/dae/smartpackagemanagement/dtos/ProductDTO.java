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
    @Positive
    private Long primaryPackageTypeId;
    private String primaryPackageType;
    @Positive
    public Long primaryPackageMeasurementUnitId;
    private String primaryPackageMeasurementUnit;
    @Positive
    public Long productCategoryId;
    private String productCategory;

    //TODO: OrderItemDTO
    //private List<OrderItemDTO> orderItems;

    private List<ProductParameterDTO> productParameters;

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
            String primaryPackageType,
            String primaryPackageMeasurementUnit,
            String productCategory,
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
        this.primaryPackageType = primaryPackageType;
        this.primaryPackageMeasurementUnit = primaryPackageMeasurementUnit;
        this.productCategory = productCategory;
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
            String reference,
            float unitStock,
            float boxStock,
            float containerStock,
            String primaryPackageType,
            String primaryPackageMeasurementUnit,
            String productCategory,
            int primaryPackQuantity,
            int secondaryPackQuantity,
            int tertiaryPackQuantity,
            List<ProductParameterDTO> productParameters
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
        this.primaryPackageType = primaryPackageType;
        this.primaryPackageMeasurementUnit = primaryPackageMeasurementUnit;
        this.productCategory = productCategory;
        this.productParameters = productParameters;
        this.primaryPackQuantity = primaryPackQuantity;
        this.secondaryPackQuantity = secondaryPackQuantity;
        this.tertiaryPackQuantity = tertiaryPackQuantity;
    }

    public ProductDTO(
            Long id,
            String name,
            String description,
            double price,
            boolean isActive,
            String manufacturerUsername,
            String reference,
            String primaryPackageType,
            String primaryPackageMeasurementUnit,
            String productCategory
    ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.isActive = isActive;
        this.reference = reference;
        this.manufacturerUsername = manufacturerUsername;
        this.primaryPackageType = primaryPackageType;
        this.primaryPackageMeasurementUnit = primaryPackageMeasurementUnit;
        this.productCategory = productCategory;
        //this.orderItems = new ArrayList<>();
    }

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

    public Long getPrimaryPackageTypeId() {
        return primaryPackageTypeId;
    }

    public void setPrimaryPackageTypeId(Long primaryPackageTypeId) {
        this.primaryPackageTypeId = primaryPackageTypeId;
    }

    public String getPrimaryPackageType() {
        return primaryPackageType;
    }

    public void setPrimaryPackageType(String primaryPackageType) {
        this.primaryPackageType = primaryPackageType;
    }

    public Long getPrimaryPackageMeasurementUnitId() {
        return primaryPackageMeasurementUnitId;
    }

    public Long getProductCategoryId() {
        return productCategoryId;
    }

    public void setProductCategoryId(Long productCategoryId) {
        this.productCategoryId = productCategoryId;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public void setPrimaryPackageMeasurementUnitId(Long primaryPackageMeasurementUnitId) {
        this.primaryPackageMeasurementUnitId = primaryPackageMeasurementUnitId;
    }

    public String getPrimaryPackageMeasurementUnit() {
        return primaryPackageMeasurementUnit;
    }

    public void setPrimaryPackageMeasurementUnit(String primaryPackageMeasurementUnit) {
        this.primaryPackageMeasurementUnit = primaryPackageMeasurementUnit;
    }

    public List<ProductParameterDTO> getProductParameters() {
        return productParameters;
    }

    public void setProductParameters(List<ProductParameterDTO> productParameters) {
        this.productParameters = productParameters;
    }
}
