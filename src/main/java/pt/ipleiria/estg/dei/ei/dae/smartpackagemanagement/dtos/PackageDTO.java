package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.dtos;

import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.enums.PackageType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PackageDTO implements Serializable {
    private long code;
    private String material;
    private PackageType packageType;
    private List<ProductDTO> products;
    private List<SensorPackageDTO> sensorPackageMetadata;
    private boolean isActive;

    public PackageDTO() {
        this.products = new ArrayList<>();
        this.sensorPackageMetadata = new ArrayList<>();
    }

    public PackageDTO(long code, String material, PackageType packageType, boolean isActive) {
        this.code = code;
        this.material = material;
        this.packageType = packageType;
        this.products = new ArrayList<>();
        this.sensorPackageMetadata = new ArrayList<>();
        this.isActive = isActive;
    }

    public PackageDTO(long code, String material, PackageType packageType, boolean isActive, List<ProductDTO> products) {
        this(code, material, packageType, isActive);
        this.products = products;
        this.sensorPackageMetadata = new ArrayList<>();
    }

    public PackageDTO(long code, String material, PackageType packageType, boolean isActive, List<SensorPackageDTO> sensorPackageMetadata, boolean hasSensors) {
        this(code, material, packageType, isActive);
        this.products = new ArrayList<>();
        this.sensorPackageMetadata = sensorPackageMetadata;
    }

    public long getCode() {
        return code;
    }

    public void setCode(long code) {
        this.code = code;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public PackageType getPackageType() {
        return packageType;
    }

    public void setPackageType(PackageType packageType) {
        this.packageType = packageType;
    }

    public List<ProductDTO> getProducts() {
        return products;
    }

    public void setProducts(List<ProductDTO> products) {
        this.products = products;
    }

    public List<SensorPackageDTO> getSensorPackageMetadata() {
        return sensorPackageMetadata;
    }

    public void setSensorPackageMetadata(List<SensorPackageDTO> sensorPackageMetadata) {
        this.sensorPackageMetadata = sensorPackageMetadata;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
