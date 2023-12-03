package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Customer extends User implements Serializable {

    private String NIF;
    private String address;
    @OneToMany(mappedBy = "customer", cascade = CascadeType.REMOVE)
    private List<Order> orders;
    @OneToMany(mappedBy = "customer", cascade = CascadeType.REMOVE)
    private List<Product> products;

    public Customer() {
        this.orders = new ArrayList<Order>();
        this.products = new ArrayList<Product>();
    }

    public Customer(String username, String password, String name, String email, String NIF, String address) {
        super(username, password, name, email);
        this.NIF = NIF;
        this.address = address;
        this.orders = new ArrayList<Order>();
        this.products = new ArrayList<Product>();
    }

    public String getNIF() {
        return NIF;
    }

    public void setNIF(String NIF) {
        this.NIF = NIF;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}
