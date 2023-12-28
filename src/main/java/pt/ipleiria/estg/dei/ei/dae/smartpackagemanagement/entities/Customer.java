package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@NamedQueries({
        @NamedQuery(
                name = "getCustomers",
                query = "SELECT c FROM Customer c ORDER BY c.name"
        ),
        @NamedQuery(
                name = "customerExists",
                query = "SELECT COUNT(c.username) FROM Customer c WHERE c.username = :username"
        )
})
public class Customer extends User implements Serializable {
    private String nif;
    private String address;
    @OneToMany(mappedBy = "customer", cascade = CascadeType.REMOVE)
    private List<Order> orders;

    public Customer() {
        this.orders = new ArrayList<Order>();
    }

    public Customer(String username, String password, String name, String email, String nif, String address) {
        super(username, password, name, email);
        this.nif = nif;
        this.address = address;
        this.orders = new ArrayList<Order>();
    }

    public String getNif() {
        return nif;
    }

    public void setNif(String nif) {
        this.nif = nif;
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
}
