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
    private SensorPackageDTO sensorPackageDTO;

    public MeasurementDTO() {
    }

    public MeasurementDTO(String measurement, Instant timestamp, SensorPackageDTO sensorPackageDTO) {
        this.measurement = measurement;
        this.timestamp = timestamp;
        this.sensorPackageDTO = sensorPackageDTO;
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

    public SensorPackageDTO getSensorPackageDTO() {
        return sensorPackageDTO;
    }

    public void setSensorPackageDTO(SensorPackageDTO sensorPackageDTO) {
        this.sensorPackageDTO = sensorPackageDTO;
    }
}
