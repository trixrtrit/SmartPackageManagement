package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.dtos;

import java.io.Serializable;

public class ProductParameterDTO implements Serializable {
    private Long id;

    private Long productId;

    private Long sensorTypeId;

    private SensorTypeDTO sensorType;

    private float minValue;

    private float maxValue;

    public ProductParameterDTO() {
    }

    public ProductParameterDTO(Long id, Long productId, Long sensorTypeId, float minValue, float maxValue) {
        this.id = id;
        this.productId = productId;
        this.sensorTypeId = sensorTypeId;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.sensorType = null;
    }

    public ProductParameterDTO(Long id, Long productId, Long sensorTypeId, float minValue, float maxValue, SensorTypeDTO sensorType) {
        this.id = id;
        this.productId = productId;
        this.sensorTypeId = sensorTypeId;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.sensorType = sensorType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long product) {
        this.productId = product;
    }

    public Long getSensorTypeId() {
        return sensorTypeId;
    }

    public void setSensorTypeId(Long sensorType) {
        this.sensorTypeId = sensorType;
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

    public SensorTypeDTO getSensorType() {
        return sensorType;
    }
}
