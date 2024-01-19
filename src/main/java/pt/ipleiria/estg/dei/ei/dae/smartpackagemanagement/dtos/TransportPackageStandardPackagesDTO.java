package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.dtos;

import java.io.Serializable;
import java.util.Date;

public class TransportPackageStandardPackagesDTO implements Serializable {

    private long id;
    private PackageDTO aPackage;
    private TransportPackageDTO transportPackage;
    private Date addedAt;
    private Date removedAt;

    public TransportPackageStandardPackagesDTO() {
    }

    public TransportPackageStandardPackagesDTO(long id, PackageDTO aPackage, Date addedAt, Date removedAt) {
        this.id = id;
        this.aPackage = aPackage;
        this.addedAt = addedAt;
        this.removedAt = removedAt;
    }

    public TransportPackageStandardPackagesDTO(
            long id,
            TransportPackageDTO transportPackage,
            Date addedAt,
            Date removedAt
    ) {
        this.id = id;
        this.transportPackage = transportPackage;
        this.addedAt = addedAt;
        this.removedAt = removedAt;
    }

    public TransportPackageStandardPackagesDTO(
            long id,
            TransportPackageDTO transportPackage,
            PackageDTO aPackage,
            Date addedAt,
            Date removedAt
    ) {
        this.id = id;
        this.aPackage = aPackage;
        this.transportPackage = transportPackage;
        this.addedAt = addedAt;
        this.removedAt = removedAt;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public PackageDTO getaPackage() {
        return aPackage;
    }

    public void setaPackage(PackageDTO aPackage) {
        this.aPackage = aPackage;
    }

    public TransportPackageDTO getTransportPackage() {
        return transportPackage;
    }

    public void setTransportPackage(TransportPackageDTO transportPackage) {
        this.transportPackage = transportPackage;
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
}
