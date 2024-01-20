package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.dtos;

import java.io.Serializable;

public class TopCustomerDTO implements Serializable {

    private String username;
    private long totalOrders;
    private double totalValue;

    public TopCustomerDTO(String username, long totalOrders, double totalValue) {
        this.username = username;
        this.totalOrders = totalOrders;
        this.totalValue = totalValue;
    }

    public String getUsername() {
        return username;
    }

    public long getTotalOrders() {
        return totalOrders;
    }

    public double getTotalValue() {
        return totalValue;
    }
}
