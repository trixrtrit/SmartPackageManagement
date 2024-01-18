package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.dtos;

import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.enums.PackageType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class StandardPackageDTO extends PackageDTO implements Serializable {

    private List<ProductDTO> products;
    public StandardPackageDTO() {
        this.products = new ArrayList<>();
    }

    public StandardPackageDTO(long code, String material, PackageType packageType, boolean isActive) {
        super(code, material, packageType, isActive);
    }

    public StandardPackageDTO(long code, String material, PackageType packageType, boolean isActive, List<ProductDTO> products) {
        super(code, material, packageType, isActive);
        this.products = products;
    }

    public StandardPackageDTO(long code, String material, PackageType packageType, boolean isActive, List<SensorPackageDTO> sensorPackageMetadata, boolean hasSensors) {
        super(code, material, packageType, isActive, sensorPackageMetadata);
        this.products = new ArrayList<>();
    }

    public List<ProductDTO> getProducts() {
        return products;
    }

    public void setProducts(List<ProductDTO> products) {
        this.products = products;
    }
}
