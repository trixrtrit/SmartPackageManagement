package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.dtos;

import java.util.ArrayList;
import java.util.List;

public class SensorDTO {
    private Long id;
    private String name;
    private List<MeasurementDTO> measurements;
    private PackageDTO aPackage;
    private SensorTypeDTO sensorType;

    public SensorDTO() {
        this.measurements = new ArrayList<MeasurementDTO>();
    }

    public SensorDTO(Long id, String name, SensorTypeDTO sensorType, PackageDTO aPackage) {
        this.id = id;
        this.name = name;
        this.sensorType = sensorType;
        this.measurements = new ArrayList<MeasurementDTO>();
        this.aPackage = aPackage;
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

    public PackageDTO getaPackage() {
        return aPackage;
    }

    public void setaPackage(PackageDTO aPackage) {
        this.aPackage = aPackage;
    }
}
