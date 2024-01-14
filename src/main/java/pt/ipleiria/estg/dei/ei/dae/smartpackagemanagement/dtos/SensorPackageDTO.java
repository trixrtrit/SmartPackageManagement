package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.dtos;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class SensorPackageDTO implements Serializable {

    private long id;
    private List<MeasurementDTO> measurements;
    private long sensorId;
    private long packageId;
    private Instant addedAt;
    private Instant removedAt;

    public SensorPackageDTO() {
        this.measurements = new ArrayList<>();
    }

    public SensorPackageDTO(long id, long sensorId, long packageId, Instant addedAt, Instant removedAt) {
        this.id = id;
        this.sensorId = sensorId;
        this.packageId = packageId;
        this.addedAt = addedAt;
        this.removedAt = removedAt;
        this.measurements = new ArrayList<>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<MeasurementDTO> getMeasurements() {
        return measurements;
    }

    public void setMeasurements(List<MeasurementDTO> measurements) {
        this.measurements = measurements;
    }

    public long getSensorId() {
        return sensorId;
    }

    public void setSensorId(long sensorId) {
        this.sensorId = sensorId;
    }

    public long getPackageId() {
        return packageId;
    }

    public void setPackageId(long packageId) {
        this.packageId = packageId;
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
}
