package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "product_categories",
        uniqueConstraints = @UniqueConstraint(columnNames = {"category"})
)
@NamedQueries({
        @NamedQuery(
                name = "getProductCategories",
                query = "SELECT pc FROM ProductCategory pc ORDER BY pc.category"
        )
})
public class ProductCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    public String category;

    @OneToMany(mappedBy = "productCategory", cascade = CascadeType.DETACH)
    private List<Product> products;

    public ProductCategory() {
        this.products = new ArrayList<>();
    }

    public ProductCategory(String category) {
        this.id = id;
        this.category = category;
        this.products = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}
