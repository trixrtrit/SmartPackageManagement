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
    private String type;
    private String measurementUnit;
    @OneToMany(mappedBy = "sensor", cascade = CascadeType.REMOVE)
    private List<Measurement> measurements;
    @ManyToOne
    @JoinColumn(name = "package_id")
    private Package aPackage;

    public Sensor() {
        this.measurements = new ArrayList<Measurement>();
    }

    public Sensor(Long id, String name, String type, String measurementUnit, Package aPackage) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.measurementUnit = measurementUnit;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMeasurementUnit() {
        return measurementUnit;
    }

    public void setMeasurementUnit(String measurementUnit) {
        this.measurementUnit = measurementUnit;
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
