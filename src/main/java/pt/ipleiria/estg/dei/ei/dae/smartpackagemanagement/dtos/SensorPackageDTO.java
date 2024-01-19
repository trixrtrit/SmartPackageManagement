package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.dtos;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SensorPackageDTO implements Serializable {

    private long id;
    private PackageDTO aPackage;
    private SensorDTO sensor;
    private Date addedAt;
    private Date removedAt;

    public SensorPackageDTO() {
    }

    public SensorPackageDTO(long id, SensorDTO sensor, Date addedAt, Date removedAt) {
        this.id = id;
        this.sensor = sensor;
        this.addedAt = addedAt;
        this.removedAt = removedAt;
    }

    public SensorPackageDTO(long id, PackageDTO aPackage, Date addedAt, Date removedAt) {
        this.id = id;
        this.aPackage = aPackage;
        this.addedAt = addedAt;
        this.removedAt = removedAt;
    }

    public SensorPackageDTO(long id, PackageDTO aPackage, SensorDTO sensor, Date addedAt, Date removedAt) {
        this.id = id;
        this.aPackage = aPackage;
        this.sensor = sensor;
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

    public Date getAddedAt() {
        return addedAt;
    }

    public void setAddedAt(Date addedAt) {
        this.addedAt = addedAt;
    }

    public Date getRemovedAt() {
        return removedAt;
    }

    public void setRemovedAt(Date removedAt) {
        this.removedAt = removedAt;
    }

    public PackageDTO getaPackage() {
        return aPackage;
    }

    public void setaPackage(PackageDTO aPackage) {
        this.aPackage = aPackage;
    }
}
