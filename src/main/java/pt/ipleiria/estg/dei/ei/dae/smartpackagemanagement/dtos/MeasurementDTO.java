package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.dtos;

import jakarta.validation.constraints.NotBlank;

import java.time.Instant;
import java.util.Date;

public class MeasurementDTO {
    private double measurement;
    private Instant timestamp;
    @NotBlank
    private long sensorId;
    @NotBlank
    private long packageCode;

    public MeasurementDTO() {
    }

    public MeasurementDTO(double measurement, Instant timestamp, long sensorId, long packageCode) {
        this.measurement = measurement;
        this.timestamp = timestamp;
        this.sensorId = sensorId;
        this.packageCode = packageCode;
    }

    public double getMeasurement() {
        return measurement;
    }

    public void setMeasurement(double measurement) {
        this.measurement = measurement;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public long getSensorId() {
        return sensorId;
    }

    public void setSensorId(long sensorId) {
        this.sensorId = sensorId;
    }

    public long getPackageCode() {
        return packageCode;
    }

    public void setPackageCode(long packageCode) {
        this.packageCode = packageCode;
    }
}
