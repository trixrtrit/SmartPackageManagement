package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.dtos;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class SensorPackageDTO implements Serializable {

    private long id;
    private PackageDTO aPackage;
    private SensorDTO sensor;
    private Instant addedAt;
    private Instant removedAt;

    public SensorPackageDTO() {
    }

    public SensorPackageDTO(long id, SensorDTO sensor, Instant addedAt, Instant removedAt) {
        this.id = id;
        this.sensor = sensor;
        this.addedAt = addedAt;
        this.removedAt = removedAt;
    }

    public SensorPackageDTO(long id, PackageDTO aPackage, Instant addedAt, Instant removedAt) {
        this.id = id;
        this.aPackage = aPackage;
        this.addedAt = addedAt;
        this.removedAt = removedAt;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public SensorDTO getSensor() {
        return sensor;
    }

    public void setSensor(SensorDTO sensor) {
        this.sensor = sensor;
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

    public PackageDTO getaPackage() {
        return aPackage;
    }

    public void setaPackage(PackageDTO aPackage) {
        this.aPackage = aPackage;
    }
}
