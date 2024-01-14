package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.dtos;

import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;
import java.time.Instant;
import java.util.Date;

public class MeasurementDTO implements Serializable {
    private String measurement;
    @Temporal(TemporalType.TIMESTAMP)
    private Instant timestamp;
    @NotBlank
    private long sensorId;
    @NotBlank
    private long packageCode;

    public MeasurementDTO() {
    }

    public MeasurementDTO(String measurement, Instant timestamp, long sensorId, long packageCode) {
        this.measurement = measurement;
        this.timestamp = timestamp;
        this.sensorId = sensorId;
        this.packageCode = packageCode;
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
