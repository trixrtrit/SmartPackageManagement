package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
public class LogisticsOperator extends User implements Serializable {
    @OneToMany(mappedBy = "logisticsOperator", cascade = CascadeType.REMOVE)
    private List<Order> orders;

    public LogisticsOperator() {
        this.orders = new ArrayList<Order>();
    }

    public LogisticsOperator(String username, String password, String name, String email) {
        super(username, password, name, email);
        this.orders = new ArrayList<Order>();
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }
}
