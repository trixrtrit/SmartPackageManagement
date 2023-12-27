package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities;

import java.io.Serializable;
import java.util.Objects;

public class ProductParameterKey implements Serializable {
    private Long product;
    private Long sensorType;

    public ProductParameterKey() {
    }

    public ProductParameterKey(Long product, Long sensorType) {
        this.product = product;
        this.sensorType = sensorType;
    }

    public Long getProduct() {
        return product;
    }

    public void setProduct(Long product) {
        this.product = product;
    }

    public Long getSensorType() {
        return sensorType;
    }

    public void setSensorType(Long sensorType) {
        this.sensorType = sensorType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductParameterKey that = (ProductParameterKey) o;
        return Objects.equals(product, that.product) &&
                Objects.equals(sensorType, that.sensorType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(product, sensorType);
    }
}
