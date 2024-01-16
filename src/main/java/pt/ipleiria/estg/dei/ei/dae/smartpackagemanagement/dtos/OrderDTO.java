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
    private String phoneNumber;
    private String postCode;
    private String city;
    private double totalPrice;
    private Date date;
    private OrderStatus status;
    private CustomerDTO customer;
    private List<OrderItemDTO> orderItems;

    public OrderDTO() {
        orderItems = new ArrayList<>();
    }

    public OrderDTO(Long id, String address, String phoneNumber, String postCode, String city, double totalPrice, Date date, OrderStatus status, CustomerDTO customer) {
        this(id, address, phoneNumber, postCode, city, totalPrice, date, status, customer, new ArrayList<>());
    }

    public OrderDTO(Long id, String address, String phoneNumber, String postCode, String city, double totalPrice, Date date, OrderStatus status, CustomerDTO customer, List<OrderItemDTO> orderItems) {
        this.id = id;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.postCode = postCode;
        this.city = city;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
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
