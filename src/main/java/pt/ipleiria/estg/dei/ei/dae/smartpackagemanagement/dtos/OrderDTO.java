package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.dtos;

import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.Customer;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.OrderItem;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.enums.OrderStatus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrderDTO implements Serializable {
    private Long id;
    private String address;
    private double totalPrice;
    private Date date;
    private OrderStatus status;
    private CustomerDTO customer;
    private List<OrderItemDTO> orderItems;

    public OrderDTO() {
        orderItems = new ArrayList<>();
    }

    public OrderDTO(Long id, String address, double totalPrice, Date date, OrderStatus status, CustomerDTO customer) {
        this(id, address, totalPrice, date, status, customer, new ArrayList<>());
    }

    public OrderDTO(Long id, String address, double totalPrice, Date date, OrderStatus status, CustomerDTO customer, List<OrderItemDTO> orderItems) {
        this.id = id;
        this.address = address;
        this.totalPrice = totalPrice;
        this.date = date;
        this.status = status;
        this.customer = customer;
        this.orderItems = orderItems;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public CustomerDTO getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerDTO customer) {
        this.customer = customer;
    }

    public List<OrderItemDTO> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItemDTO> orderItems) {
        this.orderItems = orderItems;
    }
}
