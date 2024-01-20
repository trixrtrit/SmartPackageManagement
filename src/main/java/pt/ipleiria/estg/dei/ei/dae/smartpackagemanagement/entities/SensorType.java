package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities;

import jakarta.persistence.*;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.enums.MeasurementType;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sensorTypes")
@NamedQueries({
        @NamedQuery(
                name = "getSensorTypes",
                query = "SELECT st FROM SensorType st"
        )
})
public class SensorType extends Versionable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String measurementUnit;
    @Enumerated(EnumType.STRING)
    private MeasurementType measurementType;


    @OneToMany(mappedBy = "sensorType", cascade = CascadeType.REMOVE)
    private List<Sensor> sensors;

    @OneToMany(mappedBy = "sensorType", cascade = CascadeType.REMOVE)
    private List<ProductParameter> productParameters;

    public SensorType() {
        this.sensors = new ArrayList<Sensor>();
        this.productParameters = new ArrayList<ProductParameter>();
    }

    public SensorType(String name, String measurementUnit, MeasurementType measurementType) {
        this.name = name;
        this.measurementUnit = measurementUnit;
        this.measurementType = measurementType;
        this.sensors = new ArrayList<Sensor>();
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

    public List<Sensor> getSensors() {
        return sensors;
    }

    public void setSensors(List<Sensor> sensors) {
        this.sensors = sensors;
    }

    public List<ProductParameter> getProductParameters() {
        return productParameters;
    }

    public void setProductParameters(List<ProductParameter> productParameters) {
        this.productParameters = productParameters;
    }

    public MeasurementType getMeasurementType() {
        return measurementType;
    }

    public void setMeasurementType(MeasurementType measurementType) {
        this.measurementType = measurementType;
    }
}
