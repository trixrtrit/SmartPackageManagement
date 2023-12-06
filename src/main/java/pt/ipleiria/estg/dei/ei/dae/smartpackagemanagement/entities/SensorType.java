package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sensorTypes")
public class SensorType extends Versionable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String measurementUnit;

    @OneToMany(mappedBy = "sensorType", cascade = CascadeType.REMOVE)
    private List<Sensor> sensors;

    public SensorType() {
        this.sensors = new ArrayList<Sensor>();
    }

    public SensorType(Long id, String name, String measurementUnit) {
        this.id = id;
        this.name = name;
        this.measurementUnit = measurementUnit;
        this.sensors = new ArrayList<Sensor>();
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
}
