package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.dtos;

public class ProductParameterDTO {

    private Long productId;

    private Long sensorTypeId;

    private float minValue;

    private float maxValue;

    public ProductParameterDTO() {
    }

    public ProductParameterDTO(Long productId, Long sensorTypeId, float minValue, float maxValue) {
        this.productId = productId;
        this.sensorTypeId = sensorTypeId;
        this.minValue = minValue;
        this.maxValue = maxValue;
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
}
