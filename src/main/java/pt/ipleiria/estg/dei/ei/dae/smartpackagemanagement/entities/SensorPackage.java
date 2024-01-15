package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.ArrayList;
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
    private Instant addedAt;
    @Temporal(TemporalType.TIMESTAMP)
    private Instant removedAt;

    public SensorPackage() {
        this.measurements = new ArrayList<Measurement>();
    }

    public SensorPackage(Sensor sensor, Package aPackage, Instant addedAt) {
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

    public Instant getAddedAt() {
        return addedAt;
    }

    public void setAddedAt(Instant addedAt) {
        this.addedAt = addedAt;
    }

    public Instant getRemovedAt() {
        return removedAt;
    }

    public void setRemovedAt(Instant removedAt) {
        this.removedAt = removedAt;
    }

    public List<Measurement> getMeasurements() {
        return measurements;
    }

    public void setMeasurements(List<Measurement> measurements) {
        this.measurements = measurements;
    }
}
