package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "primary_package_measurement_units",
        uniqueConstraints = @UniqueConstraint(columnNames = {"unit"})
)
@NamedQueries({
        @NamedQuery(
                name = "getPrimaryPackageMeasurementUnits",
                query = "SELECT ppmu FROM PrimaryPackageMeasurementUnit ppmu ORDER BY ppmu.unit"
        )
})
public class PrimaryPackageMeasurementUnit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    public String unit;

    @OneToMany(mappedBy = "primaryPackageMeasurementUnit", cascade = CascadeType.DETACH)
    private List<Product> products;

    public PrimaryPackageMeasurementUnit() {
        this.products = new ArrayList<>();
    }

    public PrimaryPackageMeasurementUnit(String unit) {
        this.id = id;
        this.unit = unit;
        this.products = new ArrayList<>();
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

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}
