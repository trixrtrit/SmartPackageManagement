package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.dtos;

import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.Measurement;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.SensorType;

import java.util.ArrayList;
import java.util.List;

public class SensorDTO {
    private Long id;
    private String name;
    private List<MeasurementDTO> measurements;
    private List<PackageDTO> packages;
    private SensorTypeDTO sensorType;

    public SensorDTO() {
        this.measurements = new ArrayList<MeasurementDTO>();
    }

    public SensorDTO(Long id, String name, SensorTypeDTO sensorType) {
        this.id = id;
        this.name = name;
        this.sensorType = sensorType;
        this.measurements = new ArrayList<MeasurementDTO>();
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

    public List<MeasurementDTO> getMeasurements() {
        return measurements;
    }

    public void setMeasurements(List<MeasurementDTO> measurements) {
        this.measurements = measurements;
    }

    public List<PackageDTO> getPackages() {
        return packages;
    }

    public void setPackages(List<PackageDTO> packages) {
        this.packages = packages;
    }

    public SensorTypeDTO getSensorType() {
        return sensorType;
    }

    public void setSensorType(SensorTypeDTO sensorType) {
        this.sensorType = sensorType;
    }
}
