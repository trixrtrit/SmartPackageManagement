package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

@Entity
@Table(name = "productParameters", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"product_id", "sensor_type_id"})
})
@NamedQueries({
        @NamedQuery(
                name = "getProductParameters",
                query = "SELECT pp FROM ProductParameter pp"
        )
})
public class ProductParameter extends Versionable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    @NotNull
    private Product product;

    @ManyToOne
    @JoinColumn(name = "sensor_type_id", referencedColumnName = "id")
    @NotNull
    private SensorType sensorType;

    private float minValue;

    private float maxValue;

    public ProductParameter() {
    }

    public ProductParameter(Product product, SensorType sensorType, float minValue, float maxValue) {
        this.product = product;
        this.sensorType = sensorType;
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public SensorType getSensorType() {
        return sensorType;
    }

    public void setSensorType(SensorType sensorType) {
        this.sensorType = sensorType;
    }

    public float getMinValue() {
        return minValue;
    }

    public void setMinValue(float minValue) {
        this.minValue = minValue;
    }

    public float getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(float maxValue) {
        this.maxValue = maxValue;
    }
}
