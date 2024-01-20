package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.dtos;

import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.enums.MeasurementType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SensorTypeDTO implements Serializable {
    private Long id;
    private String name;
    private String measurementUnit;
    private MeasurementType measurementType;
    private List<SensorDTO> sensors;
    private List<ProductParameterDTO> productParameters;

    public SensorTypeDTO() {
        this.sensors = new ArrayList<SensorDTO>();
        this.productParameters = new ArrayList<ProductParameterDTO>();
    }

    public SensorTypeDTO(Long id, String name, String measurementUnit, MeasurementType measurementType) {
        this.id = id;
        this.name = name;
        this.measurementUnit = measurementUnit;
        this.measurementType = measurementType;
        this.sensors = new ArrayList<SensorDTO>();
        this.productParameters = new ArrayList<>();
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

    public String getMeasurementUnit() {
        return measurementUnit;
    }

    public void setMeasurementUnit(String measurementUnit) {
        this.measurementUnit = measurementUnit;
    }

    public List<SensorDTO> getSensors() {
        return sensors;
    }

    public void setSensors(List<SensorDTO> sensors) {
        this.sensors = sensors;
    }

    public MeasurementType getMeasurementType() {
        return measurementType;
    }

    public void setMeasurementType(MeasurementType measurementType) {
        this.measurementType = measurementType;
    }

    public List<ProductParameterDTO> getProductParameters() {
        return productParameters;
    }

    public void setProductParameters(List<ProductParameterDTO> productParameters) {
        this.productParameters = productParameters;
    }
}
