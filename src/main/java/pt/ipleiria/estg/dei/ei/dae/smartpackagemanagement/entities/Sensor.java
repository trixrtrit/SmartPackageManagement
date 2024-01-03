package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities;

import jakarta.persistence.*;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
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
    @OneToMany(mappedBy = "sensor", cascade = CascadeType.REMOVE)
    private List<Measurement> measurements;
    @ManyToMany
    @JoinTable(
            name = "sensors_packages",
            joinColumns = @JoinColumn(
                    name = "sensor_id",
                    referencedColumnName = "id"
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "package_code",
                    referencedColumnName = "code"
            )
    )
    private List<Package> packages;

    @ManyToOne
    @JoinColumn(name = "sensorType_id")
    @NotNull
    private SensorType sensorType;

    public Sensor() {
        this.measurements = new ArrayList<Measurement>();
        this.packages = new ArrayList<>();
    }

    public Sensor(String name, SensorType sensorType) {
        this.name = name;
        this.sensorType = sensorType;
        this.measurements = new ArrayList<Measurement>();
        this.packages = new ArrayList<>();
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

    public List<Measurement> getMeasurements() {
        return measurements;
    }

    public void setMeasurements(List<Measurement> measurements) {
        this.measurements = measurements;
    }

    public SensorType getSensorType() {
        return sensorType;
    }

    public void setSensorType(SensorType sensorType) {
        this.sensorType = sensorType;
    }

    public List<Package> getPackages() {
        return packages;
    }

    public void setPackages(List<Package> packages) {
        this.packages = packages;
    }

    public void addPackage(Package aPackage) {
        if (!packages.contains(aPackage)) {
            packages.add(aPackage);
        }
    }
    public void removePackage(Package aPackage) {
        packages.remove(aPackage);
    }

}
