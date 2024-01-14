package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.Date;

@Entity
@Table(name = "measurements")
public class Measurement extends Versionable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String measurement;
    @Temporal(TemporalType.TIMESTAMP)
    private Instant timestamp;
    @ManyToOne
    @NotNull
    @JoinColumn(name = "sensor_package_id")
    private SensorPackage sensorPackage;

    public Measurement() {
    }

    public Measurement(String measurement, SensorPackage sensorPackage) {
        this.measurement = measurement;
        this.sensorPackage = sensorPackage;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMeasurement() {
        return measurement;
    }

    public void setMeasurement(String measurement) {
        this.measurement = measurement;
    }


    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public SensorPackage getSensorPackage() {
        return sensorPackage;
    }

    public void setSensorPackage(SensorPackage sensorPackage) {
        this.sensorPackage = sensorPackage;
    }
}
