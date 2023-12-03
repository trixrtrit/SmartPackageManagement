package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "packages")
public class Package extends Versionable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String material;
    private String type;
    @OneToMany(mappedBy = "package", cascade = CascadeType.REMOVE)
    private List<Sensor> sensors;
    @OneToMany(mappedBy = "package", cascade = CascadeType.REMOVE)
    private List<Measurement> measurements;
    @OneToMany(mappedBy = "package", cascade = CascadeType.REMOVE)
    private List<Product> products;

    @ManyToOne
    @JoinColumn(name = "manufacturer_username")
    @NotNull
    private Manufacturer manufacturer;

    public Package() {
        this.sensors = new ArrayList<Sensor>();
        this.products = new ArrayList<Product>();
        this.measurements = new ArrayList<Measurement>();
    }

    public Package(Long id, String material, String type, Manufacturer manufacturer) {
        this.id = id;
        this.material = material;
        this.type = type;
        this.manufacturer = manufacturer;
        this.sensors = new ArrayList<Sensor>();
        this.products = new ArrayList<Product>();
        this.measurements = new ArrayList<Measurement>();
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

    public Manufacturer getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(Manufacturer manufacturer) {
        this.manufacturer = manufacturer;
    }

    public List<Sensor> getSensors() {
        return sensors;
    }

    public void setSensors(List<Sensor> sensors) {
        this.sensors = sensors;
    }

    public List<Measurement> getMeasurements() {
        return measurements;
    }

    public void setMeasurements(List<Measurement> measurements) {
        this.measurements = measurements;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}
