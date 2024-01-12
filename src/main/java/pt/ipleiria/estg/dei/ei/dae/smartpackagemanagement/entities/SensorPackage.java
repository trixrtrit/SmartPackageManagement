package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "sensor_package")
public class SensorPackage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "sensorPackage", cascade = CascadeType.REMOVE)
    private List<Measurement> measurements;
    @ManyToOne
    @NotNull
    @JoinColumn(name = "sensor_id")
    private Sensor sensor;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "package_id")
    private Package aPackage;

    @Temporal(TemporalType.TIMESTAMP)
    private Date addedAt;
    @Temporal(TemporalType.TIMESTAMP)
    private Date removedAt;

    public SensorPackage() {
        this.measurements = new ArrayList<Measurement>();
    }

    public SensorPackage(Sensor sensor, Package aPackage, Date addedAt) {
        this.measurements = new ArrayList<Measurement>();
        this.sensor = sensor;
        this.aPackage = aPackage;
        this.addedAt = addedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Sensor getSensor() {
        return sensor;
    }

    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }

    public Package getaPackage() {
        return aPackage;
    }

    public void setaPackage(Package aPackage) {
        this.aPackage = aPackage;
    }

    public Date getAddedAt() {
        return addedAt;
    }

    public void setAddedAt(Date addedAt) {
        this.addedAt = addedAt;
    }

    public Date getRemovedAt() {
        return removedAt;
    }

    public void setRemovedAt(Date removedAt) {
        this.removedAt = removedAt;
    }

    public List<Measurement> getMeasurements() {
        return measurements;
    }

    public void setMeasurements(List<Measurement> measurements) {
        this.measurements = measurements;
    }
}
