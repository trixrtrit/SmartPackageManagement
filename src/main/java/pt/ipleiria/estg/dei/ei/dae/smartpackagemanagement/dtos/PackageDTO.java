package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.dtos;

import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.enums.PackageType;

import java.util.ArrayList;
import java.util.List;

public class PackageDTO {
    private Long id;
    private String material;
    private PackageType packageType;
    private List<ProductDTO> products;
    private List<SensorDTO> sensors;

    public PackageDTO() {
        this.products = new ArrayList<>();
        this.sensors = new ArrayList<>();
    }

    public PackageDTO(Long id, String material, PackageType packageType) {
        this.id = id;
        this.material = material;
        this.packageType = packageType;
        this.products = new ArrayList<>();
        this.sensors = new ArrayList<>();
    }

    public PackageDTO(Long id, String material, PackageType packageType, List<ProductDTO> products) {
        this(id, material, packageType);
        this.products = products;
        this.sensors = new ArrayList<>();
    }

    public PackageDTO(Long id, String material, PackageType packageType, List<SensorDTO> sensors, boolean hasSensors) {
        this(id, material, packageType);
        this.products = new ArrayList<>();
        this.sensors = sensors;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public List<SensorDTO> getSensors() {
        return sensors;
    }

    public void setSensors(List<SensorDTO> sensors) {
        this.sensors = sensors;
    }
}
