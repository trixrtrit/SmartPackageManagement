package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.io.Serializable;
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
        ),
        @NamedQuery(
                name = "getProductsForExport",
                query = "SELECT p FROM Product p WHERE p.isActive = true ORDER BY p.name"
        ),
        @NamedQuery(
                name = "productExists",
                query = "SELECT COUNT(p.id) FROM Product p WHERE p.id = :id"
        )
})
@SQLDelete(sql="UPDATE products SET deleted = TRUE, isactive = FALSE WHERE id = ? AND deleted = ?::boolean")
@Where(clause = "deleted IS FALSE")
public class Product extends Versionable implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
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


    @PositiveOrZero
    private int primaryPackQuantity;
    @PositiveOrZero
    private int secondaryPackQuantity;
    @PositiveOrZero
    private int tertiaryPackQuantity;

    @ManyToOne
    @JoinColumn(name = "primaryPackageMeasurementUnit")
    @NotNull
    private PrimaryPackageMeasurementUnit primaryPackageMeasurementUnit;

    @ManyToOne
    @JoinColumn(name = "primaryPackageType")
    @NotNull
    private PrimaryPackageType primaryPackageType;

    @ManyToOne
    @JoinColumn(name = "productCategory")
    @NotNull
    private ProductCategory productCategory;

    @ManyToOne
    @JoinColumn(name = "manufacturer_username")
    @NotNull
    private Manufacturer manufacturer;

    @ManyToMany
    @JoinTable(
            name = "products_packages",
            joinColumns = @JoinColumn(
                    name = "product_id",
                    referencedColumnName = "id"
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "package_code",
                    referencedColumnName = "code"
            )
    )
    private List<StandardPackage> standardPackages;
    private boolean deleted;

    @OneToMany(mappedBy = "product", cascade = CascadeType.REMOVE)
    private List<OrderItem> orderItems;

    @OneToMany(mappedBy = "product", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<ProductParameter> productParameters;

    public Product() {
        this.orderItems = new ArrayList<OrderItem>();
        this.productParameters = new ArrayList<>();
        this.standardPackages = new ArrayList<>();
    }

    public Product(
            String name,
            String description,
            double price,
            Manufacturer manufacturer,
            PrimaryPackageMeasurementUnit primaryPackageMeasurementUnit,
            PrimaryPackageType primaryPackageType,
            ProductCategory category,
            boolean isActive,
            String productReference,
            int primaryPackQuantity,
            int secondaryPackQuantity,
            int tertiaryPackQuantity
    ) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.manufacturer = manufacturer;
        this.primaryPackageMeasurementUnit = primaryPackageMeasurementUnit;
        this.primaryPackageType = primaryPackageType;
        this.productCategory = category;
        this.isActive = isActive;
        this.productReference = productReference;
        this.orderItems = new ArrayList<OrderItem>();
        this.productParameters = new ArrayList<>();
        this.standardPackages = new ArrayList<>();
        this.primaryPackQuantity = primaryPackQuantity;
        this.secondaryPackQuantity = secondaryPackQuantity;
        this.tertiaryPackQuantity = tertiaryPackQuantity;
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

    public Manufacturer getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(Manufacturer manufacturer) {
        this.manufacturer = manufacturer;
    }

    public PrimaryPackageMeasurementUnit getPrimaryPackageMeasurementUnit() {
        return primaryPackageMeasurementUnit;
    }

    public void setPrimaryPackageMeasurementUnit(PrimaryPackageMeasurementUnit primaryPackageMeasurementUnit) {
        this.primaryPackageMeasurementUnit = primaryPackageMeasurementUnit;
    }

    public PrimaryPackageType getPrimaryPackageType() {
        return primaryPackageType;
    }

    public void setPrimaryPackageType(PrimaryPackageType primaryPackageType) {
        this.primaryPackageType = primaryPackageType;
    }

    public ProductCategory getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(ProductCategory productCategory) {
        this.productCategory = productCategory;
    }

    public List<StandardPackage> getStandardPackages() {
        return standardPackages;
    }

    public void setStandardPackages(List<StandardPackage> standardPackages) {
        this.standardPackages = standardPackages;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
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

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public List<ProductParameter> getProductParameters() {
        return productParameters;
    }

    public void setProductParameters(List<ProductParameter> productParameters) {
        this.productParameters = productParameters;
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

    public void addStandardPackage(StandardPackage standardPackage) {
        if (!standardPackages.contains(standardPackage)) {
            standardPackages.add(standardPackage);
        }
    }
    public void removePackage(StandardPackage standardPackage) {
        standardPackages.remove(standardPackage);
    }
    public void addUnitStock() {
        this.unitStock = this.unitStock + 1;
    }
    public void addBoxStock() {
        this.boxStock = this.boxStock + 1;
    }
    public void addContainerStock() {
        this.containerStock = this.containerStock + 1;
    }
    public void addUnitStock(float amount) {
        this.unitStock = this.unitStock + amount;
    }
    public void addBoxStock(float amount) {
        this.boxStock = this.boxStock + amount;
    }
    public void addContainerStock(float amount) {
        this.containerStock = this.containerStock + amount;
    }

}
