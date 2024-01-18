package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.Date;

@Entity
@Table(name = "transport_standard_package")
@NamedQueries({
        @NamedQuery(
                name = "transportPackageStandardPackageAssociationExists",
                query = "SELECT COUNT(tp.id) FROM TransportPackageStandardPackage tp " +
                        "WHERE tp.standardPackage.code = :standardPkgCode AND tp.removedAt IS NULL"
        ),
        @NamedQuery(
                name = "findTransportPackageStandardPackageAssociation",
                query = "SELECT tp FROM TransportPackageStandardPackage tp " +
                        "WHERE tp.transportPackage.code = :transportPkgCode AND tp.standardPackage.code = :standardPkgCode " +
                        "AND tp.removedAt IS NULL"
        ),
})
public class TransportPackageStandardPackage extends Versionable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "standard_package_id")
    private StandardPackage standardPackage;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "transport_package_id")
    private TransportPackage transportPackage;

    @Temporal(TemporalType.TIMESTAMP)
    private Date addedAt;
    @Temporal(TemporalType.TIMESTAMP)
    private Date removedAt;

    public TransportPackageStandardPackage() {
    }

    public TransportPackageStandardPackage(StandardPackage standardPackage, TransportPackage transportPackage) {
        this.standardPackage = standardPackage;
        this.transportPackage = transportPackage;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public StandardPackage getStandardPackage() {
        return standardPackage;
    }

    public void setStandardPackage(StandardPackage standardPackage) {
        this.standardPackage = standardPackage;
    }

    public TransportPackage getTransportPackage() {
        return transportPackage;
    }

    public void setTransportPackage(TransportPackage transportPackage) {
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
