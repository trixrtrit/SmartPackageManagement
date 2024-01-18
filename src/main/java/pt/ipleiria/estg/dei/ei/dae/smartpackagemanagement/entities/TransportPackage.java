package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.ArrayList;
import java.util.List;

@Entity
@NamedQueries({
        @NamedQuery(
                name = "packageExists",
                query = "SELECT COUNT(p.code) FROM TransportPackage p WHERE p.code = :code"
        )
})
@SQLDelete(sql="UPDATE packages SET deleted = TRUE WHERE code = ? AND deleted = ?::boolean")
@Where(clause = "deleted IS FALSE")
public class TransportPackage extends Package{
    @OneToMany(mappedBy = "transportPackage", cascade = CascadeType.REMOVE)
    private List<TransportPackageStandardPackage> transportPackageStandardPackages;

    public TransportPackage() {
        this.transportPackageStandardPackages = new ArrayList<TransportPackageStandardPackage>();
    }

    public TransportPackage(long code, String material) {
        super(code, material);
        this.transportPackageStandardPackages = new ArrayList<TransportPackageStandardPackage>();
    }

    public List<TransportPackageStandardPackage> getTransportPackageStandardPackages() {
        return transportPackageStandardPackages;
    }

    public void setTransportPackageStandardPackages(List<TransportPackageStandardPackage> transportPackageStandardPackages) {
        this.transportPackageStandardPackages = transportPackageStandardPackages;
    }
}
