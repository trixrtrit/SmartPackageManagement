package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@NamedQueries({
        @NamedQuery(
                name = "getManufacturers",
                query = "SELECT m FROM Manufacturer m ORDER BY m.name"
        ),
        @NamedQuery(
                name = "manufacturerExists",
                query = "SELECT COUNT(m.username) FROM Manufacturer m WHERE m.username = :username"
        )
})
public class Manufacturer extends User implements Serializable {
    @OneToMany(mappedBy = "manufacturer", cascade = CascadeType.REMOVE)
    private List<Product> products;

    public Manufacturer() {
        this.products = new ArrayList<Product>();
    }

    public Manufacturer(String username, String password, String name, String email) {
        super(username, password, name, email);
        this.products = new ArrayList<Product>();
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}
