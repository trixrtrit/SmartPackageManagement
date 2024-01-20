package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities;

import jakarta.persistence.*;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.enums.PackageType;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@NamedQueries({
        @NamedQuery(
                name = "standardPackageExists",
                query = "SELECT COUNT(p.code) FROM StandardPackage p WHERE p.code = :code"
        )
})
@SQLDelete(sql="UPDATE packages SET deleted = TRUE WHERE code = ? AND deleted = ?::boolean")
@Where(clause = "deleted IS FALSE")
public class StandardPackage extends Package {

    @Enumerated(EnumType.STRING)
    private PackageType packageType;

    @OneToMany(mappedBy = "standardPackage", cascade = CascadeType.REMOVE)
    private List<TransportPackageStandardPackage> transportPackageStandardPackages;
    private Long initialProductId;

    @OneToMany(mappedBy = "standardPackage", cascade = CascadeType.REMOVE)
    private List<StandardPackageProduct> standardPackageProducts;

    public StandardPackage() {
        this.transportPackageStandardPackages = new ArrayList<TransportPackageStandardPackage>();
        this.standardPackageProducts = new ArrayList<StandardPackageProduct>();
    }

    public StandardPackage(long code, String material, PackageType packageType, Date manufactureDate) {
        super(code, material, manufactureDate);
        this.packageType = packageType;
        this.transportPackageStandardPackages = new ArrayList<TransportPackageStandardPackage>();
        this.standardPackageProducts = new ArrayList<StandardPackageProduct>();
    }

    public List<TransportPackageStandardPackage> getTransportPackageStandardPackages() {
        return transportPackageStandardPackages;
    }

    public void setTransportPackageStandardPackages(List<TransportPackageStandardPackage> transportPackageStandardPackages) {
        this.transportPackageStandardPackages = transportPackageStandardPackages;
    }

    public PackageType getPackageType() {
        return packageType;
    }

    public void setPackageType(PackageType packageType) {
        this.packageType = packageType;
    }

    public List<StandardPackageProduct> getStandardPackageProducts() {
        return standardPackageProducts;
    }

    public void setStandardPackageProducts(List<StandardPackageProduct> standardPackageProducts) {
        this.standardPackageProducts = standardPackageProducts;
    }

    public Long getInitialProductId() {
        return initialProductId;
    }

    public void setInitialProductId(Long initialProductId) {
        this.initialProductId = initialProductId;
    }
}
