package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.dtos;

public class PrimaryPackageMeasurementUnitDTO {
    private Long id;
    public String unit;

    public PrimaryPackageMeasurementUnitDTO() {
    }

    public PrimaryPackageMeasurementUnitDTO(Long id, String unit) {
        this.id = id;
        this.unit = unit;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
