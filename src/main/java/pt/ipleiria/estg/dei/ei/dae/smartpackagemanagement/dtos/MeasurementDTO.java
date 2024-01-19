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
    private Date timestamp;
    private SensorPackageDTO sensorPackageDTO;

    public MeasurementDTO() {
    }

    public MeasurementDTO(String measurement, Date timestamp, SensorPackageDTO sensorPackageDTO) {
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

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public SensorPackageDTO getSensorPackageDTO() {
        return sensorPackageDTO;
    }

    public void setSensorPackageDTO(SensorPackageDTO sensorPackageDTO) {
        this.sensorPackageDTO = sensorPackageDTO;
    }
}
