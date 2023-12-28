package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.dtos;

import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.Measurement;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.SensorType;

import java.util.ArrayList;
import java.util.List;

public class SensorDTO {

    private Long id;
    private String name;
    private List<Measurement> measurements;
    private long packageId;
    private SensorType sensorType;

    public SensorDTO() {
        this.measurements = new ArrayList<Measurement>();
    }
    public SensorDTO(Long id, String name, SensorType sensorType, long packageId) {
        this.id = id;
        this.name = name;
        this.sensorType = sensorType;
        this.packageId = packageId;
        this.measurements = new ArrayList<Measurement>();
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
    public long getPackageId() {
        return packageId;
    }

    public void setPackageId(long packageId) {
        this.packageId = packageId;
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
}
