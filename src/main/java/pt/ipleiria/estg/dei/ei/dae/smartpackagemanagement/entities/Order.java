package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.enums.OrderStatus;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "orders")
@NamedQueries({
        @NamedQuery(
                name = "getOrders",
                query = "SELECT c FROM Order c ORDER BY c.date DESC"
        ),
        @NamedQuery(
                name = "getCustomerOrders",
                query = "SELECT c FROM Order c WHERE c.customer.username = :username ORDER BY c.date DESC"
        )
})
public class Order extends Versionable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String address;
    private double totalPrice;
    private Date date;
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @ManyToOne
    @JoinColumn(name = "customer_username")
    @NotNull
    private Customer customer;

    @OneToMany(mappedBy = "order", cascade = CascadeType.REMOVE)
    private List<OrderItem> orderItems;

    public Order() {
        this.orderItems = new ArrayList<OrderItem>();
    }

    public Order(String address, double totalPrice, Date date, OrderStatus status, Customer customer, List<OrderItem> orderItems) {
        this.id = id;
        this.address = address;
        this.totalPrice = totalPrice;
        this.date = date;
        this.status = status;
        this.customer = customer;
        this.orderItems = orderItems;
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

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }
}
