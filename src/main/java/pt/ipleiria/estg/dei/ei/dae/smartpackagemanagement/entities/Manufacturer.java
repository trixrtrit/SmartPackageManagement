package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Manufacturer extends User implements Serializable {
    @OneToMany(mappedBy = "manufacturer", cascade = CascadeType.REMOVE)
    private List<Product> products;

    @OneToMany(mappedBy = "manufacturer", cascade = CascadeType.REMOVE)
    private List<Package> packages;

    public Manufacturer() {
        this.products = new ArrayList<Product>();
        this.packages = new ArrayList<Package>();
    }

    public Manufacturer(String username, String password, String name, String email) {
        super(username, password, name, email);
        this.products = new ArrayList<Product>();
        this.packages = new ArrayList<Package>();
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public List<Package> getPackages() {
        return packages;
    }

    public void setPackages(List<Package> packages) {
        this.packages = packages;
    }
}
