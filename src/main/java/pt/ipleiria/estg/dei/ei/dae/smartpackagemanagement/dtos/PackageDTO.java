package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.dtos;

public class PackageDTO {
    private Long id;
    private String material;
    private String type;

    public PackageDTO() {
    }

    public PackageDTO(Long id, String material, String type) {
        this.id = id;
        this.material = material;
        this.type = type;
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
}
