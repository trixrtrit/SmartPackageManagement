package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.dtos;

import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.enums.PackageType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class StandardPackageDTO extends PackageDTO implements Serializable {

    private PackageType packageType;
    private List<ProductDTO> products;
    public StandardPackageDTO() {
        this.products = new ArrayList<>();
    }

    public StandardPackageDTO(long code, String material, PackageType packageType, boolean isActive) {
        super(code, material, isActive);
        this.packageType = packageType;
    }

    public StandardPackageDTO(long code, String material, PackageType packageType, boolean isActive, List<ProductDTO> products) {
        super(code, material, isActive);
        this.products = products;
        this.packageType = packageType;
    }

    public StandardPackageDTO(long code, String material, PackageType packageType, boolean isActive, List<SensorPackageDTO> sensorPackageMetadata, boolean hasSensors) {
        super(code, material, isActive, sensorPackageMetadata);
        this.products = new ArrayList<>();
        this.packageType = packageType;
    }

    public List<ProductDTO> getProducts() {
        return products;
    }

    public void setProducts(List<ProductDTO> products) {
        this.products = products;
    }

    public PackageType getPackageType() {
        return packageType;
    }

    public void setPackageType(PackageType packageType) {
        this.packageType = packageType;
    }
}
