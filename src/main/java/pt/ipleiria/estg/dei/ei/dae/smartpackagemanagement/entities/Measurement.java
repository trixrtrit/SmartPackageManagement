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
    private String measurement;
    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;
    @ManyToOne
    @NotNull
    @JoinColumn(name = "sensor_package_id")
    private SensorPackage sensorPackage;

    @OneToOne(mappedBy = "measurement", cascade = CascadeType.ALL,optional = true)
    private Notification notification;

    public Measurement() {
    }

    public Measurement(String measurement, SensorPackage sensorPackage) {
        this.measurement = measurement;
        this.sensorPackage = sensorPackage;
        this.timestamp = new Date();
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


    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public SensorPackage getSensorPackage() {
        return sensorPackage;
    }

    public void setSensorPackage(SensorPackage sensorPackage) {
        this.sensorPackage = sensorPackage;
    }

    public Notification getNotification() {
        return notification;
    }

    public void setNotification(Notification notification) {
        this.notification = notification;
    }
}
