package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.dtos;

import java.util.Date;

public class MeasurementDTO {
    private Long id;
    private double measurement;
    private Date timestamp;
    private SensorDTO sensor;

    private PackageDTO aPackage;

    public MeasurementDTO() {
    }

    public MeasurementDTO(Long id, double measurement, Date timestamp, SensorDTO sensor, PackageDTO aPackage) {
        this.id = id;
        this.measurement = measurement;
        this.timestamp = timestamp;
        this.sensor = sensor;
        this.aPackage = aPackage;
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

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public SensorDTO getSensor() {
        return sensor;
    }

    public void setSensor(SensorDTO sensor) {
        this.sensor = sensor;
    }

    public PackageDTO getaPackage() {
        return aPackage;
    }

    public void setaPackage(PackageDTO aPackage) {
        this.aPackage = aPackage;
    }
}
