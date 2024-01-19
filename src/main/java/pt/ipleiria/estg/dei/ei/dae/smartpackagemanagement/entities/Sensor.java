package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities;

import jakarta.persistence.*;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;
@Entity
@Table(name = "sensors")
@NamedQueries({
        @NamedQuery(
                name = "getSensors",
                query = "SELECT s FROM Sensor s ORDER BY s.name"
        )
})
public class Sensor extends Versionable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private boolean isAvailable = true;
    @OneToMany(mappedBy = "sensor", cascade = CascadeType.REMOVE)
    private List<SensorPackage> sensorPackageList;

    @ManyToOne
    @JoinColumn(name = "sensorType_id")
    @NotNull
    private SensorType sensorType;

    public Sensor() {
        this.sensorPackageList = new ArrayList<>();
    }

    public Sensor(String name, SensorType sensorType) {
        this.name = name;
        this.sensorType = sensorType;
        this.sensorPackageList = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SensorType getSensorType() {
        return sensorType;
    }

    public void setSensorType(SensorType sensorType) {
        this.sensorType = sensorType;
    }

    public List<SensorPackage> getSensorPackageList() {
        return sensorPackageList;
    }

    public void setSensorPackageList(List<SensorPackage> sensorPackageList) {
        this.sensorPackageList = sensorPackageList;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }
}
