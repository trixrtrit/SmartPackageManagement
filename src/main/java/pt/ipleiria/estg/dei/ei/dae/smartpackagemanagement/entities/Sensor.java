package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities;

import jakarta.persistence.*;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
@Entity
@Table(name = "sensors")
public class Sensor extends Versionable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @OneToMany(mappedBy = "sensor", cascade = CascadeType.REMOVE)
    private List<Measurement> measurements;
    @ManyToOne
    @JoinColumn(name = "package_id")
    private Package aPackage;

    @ManyToOne
    @JoinColumn(name = "sensorType_id")
    private SensorType sensorType;

    public Sensor() {
        this.measurements = new ArrayList<Measurement>();
    }

    public Sensor(Long id, String name, SensorType sensorType, Package aPackage) {
        this.id = id;
        this.name = name;
        this.sensorType = sensorType;
        this.measurements = new ArrayList<Measurement>();
        this.aPackage = aPackage;
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

    public List<Measurement> getMeasurements() {
        return measurements;
    }

    public void setMeasurements(List<Measurement> measurements) {
        this.measurements = measurements;
    }

    public Package getaPackage() {
        return aPackage;
    }

    public void setaPackage(Package aPackage) {
        this.aPackage = aPackage;
    }
}
