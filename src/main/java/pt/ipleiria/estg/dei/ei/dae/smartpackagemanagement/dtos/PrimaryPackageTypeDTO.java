package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.dtos;

import jakarta.persistence.*;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.Product;

import java.io.Serializable;
import java.util.List;

public class PrimaryPackageTypeDTO  implements Serializable {
    private Long id;
    public String type;

    public PrimaryPackageTypeDTO() {
    }

    public PrimaryPackageTypeDTO(Long id, String type) {
        this.id = id;
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
