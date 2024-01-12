package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.Date;

@Entity
@Table(name = "measurements")
public class Measurement extends Versionable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double measurement;
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @ManyToOne
    @NotNull
    @JoinColumn(name = "sensor_package_id")
    private SensorPackage sensorPackage;

    public Measurement() {
    }

    public Measurement(double measurement, SensorPackage sensorPackage) {
        this.measurement = measurement;
        this.sensorPackage = sensorPackage;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getMeasurement() {
        return measurement;
    }

    public void setMeasurement(double measurement) {
        this.measurement = measurement;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public SensorPackage getSensorPackage() {
        return sensorPackage;
    }

    public void setSensorPackage(SensorPackage sensorPackage) {
        this.sensorPackage = sensorPackage;
    }
}
