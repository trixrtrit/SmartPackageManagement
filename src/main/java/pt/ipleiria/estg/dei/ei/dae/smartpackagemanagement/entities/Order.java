package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order extends Versionable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String address;
    private double totalPrice;
    private Date date;
    private String status;

    @ManyToOne
    @JoinColumn(name = "customer_username")
    @NotNull
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "logisticsOperator_username")
    @NotNull
    private LogisticsOperator logisticsOperator;

    @OneToMany(mappedBy = "order", cascade = CascadeType.REMOVE)
    private List<OrderItem> orderItems;

    public Order() {
        this.orderItems = new ArrayList<OrderItem>();
    }

    public Order(long id, String address, double totalPrice, Date date, String status, Customer customer, LogisticsOperator logisticsOperator) {
        this.id = id;
        this.address = address;
        this.totalPrice = totalPrice;
        this.date = date;
        this.status = status;
        this.customer = customer;
        this.logisticsOperator = logisticsOperator;
        this.orderItems = new ArrayList<OrderItem>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public LogisticsOperator getLogisticsOperator() {
        return logisticsOperator;
    }

    public void setLogisticsOperator(LogisticsOperator logisticsOperator) {
        this.logisticsOperator = logisticsOperator;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }
}
