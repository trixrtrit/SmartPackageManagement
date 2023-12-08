package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.dtos;

import java.util.Date;

public class OrderDTO {
    private Long id;
    private String address;
    private double totalPrice;
    private Date date;
    private String status;

    public OrderDTO() {
    }

    public OrderDTO(Long id, String address, double totalPrice, Date date, String status) {
        this.id = id;
        this.address = address;
        this.totalPrice = totalPrice;
        this.date = date;
        this.status = status;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
