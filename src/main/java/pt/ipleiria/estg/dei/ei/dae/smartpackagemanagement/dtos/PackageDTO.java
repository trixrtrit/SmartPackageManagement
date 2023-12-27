package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.dtos;

import java.util.ArrayList;
import java.util.List;

public class PackageDTO {
    private Long id;
    private String material;
    private String type;
    private List<ProductDTO> products;
    //private List<SensorDTO> sensors;

    public PackageDTO() {
        this.products = new ArrayList<>();
        //this.sensors = new ArrayList<>();
    }

    public PackageDTO(Long id, String material, String type) {
        this.id = id;
        this.material = material;
        this.type = type;
        this.products = new ArrayList<>();
        //this.sensors = new ArrayList<>();
    }

    public PackageDTO(Long id, String material, String type, List<ProductDTO> products) {
        this.id = id;
        this.material = material;
        this.type = type;
        this.products = products;
        //this.sensors = new ArrayList<>();
    }

    public PackageDTO(Long id, String material, String type,/* List<SensorDTO> sensors,*/ boolean hasSensors) {
        this.id = id;
        this.material = material;
        this.type = type;
        this.products = new ArrayList<>();
        //this.sensors = sensors;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<ProductDTO> getProducts() {
        return products;
    }

    public void setProducts(List<ProductDTO> products) {
        this.products = products;
    }
/*
    public List<SensorDTO> getSensors() {
        return sensors;
    }

    public void setSensors(List<SensorDTO> sensors) {
        this.sensors = sensors;
    }*/
}
