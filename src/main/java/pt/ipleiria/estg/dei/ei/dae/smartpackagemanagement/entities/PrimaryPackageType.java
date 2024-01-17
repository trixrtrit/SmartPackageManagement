package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "primary_package_types",
        uniqueConstraints = @UniqueConstraint(columnNames = {"type"})
)
@NamedQueries({
        @NamedQuery(
                name = "getPrimaryPackageTypes",
                query = "SELECT ppt FROM PrimaryPackageType ppt ORDER BY ppt.type"
        )
})
public class PrimaryPackageType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    public String type;

    @OneToMany(mappedBy = "primaryPackageType", cascade = CascadeType.DETACH)
    private List<Product> products;

    public PrimaryPackageType() {
        products = new ArrayList<>();
    }

    public PrimaryPackageType(String type) {
        this.type = type;
        products = new ArrayList<>();
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

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}
