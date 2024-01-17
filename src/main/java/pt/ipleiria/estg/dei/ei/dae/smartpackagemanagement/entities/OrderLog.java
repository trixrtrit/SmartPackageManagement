package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.enums.OrderStatus;

import java.time.Instant;

@Entity
@Table(name = "order_logs")
public class OrderLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String logEntry;
    @Temporal(TemporalType.TIMESTAMP)
    private Instant timestamp;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "order_id")
    private Order order;
    private OrderStatus orderStatus;
    private String customerUsername;
    private String logisticsOperatorUsername;

    public OrderLog(){

    }

    public OrderLog(String logEntry, Order order, OrderStatus newStatus, String logisticsOperatorUsername) {
        this.logEntry = logEntry;
        this.order = order;
        this.orderStatus = newStatus;
        this.customerUsername = order.getCustomer().getUsername();
        this.logisticsOperatorUsername = logisticsOperatorUsername;
    }

    public Long getId() {
        return id;
    }

//    public void setId(Long id) {
//        this.id = id;
//    }

    public String getLogEntry() {
        return logEntry;
    }

//    public void setLogEntry(String logEntry) {
//        this.logEntry = logEntry;
//    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public Order getOrder() {
        return order;
    }

//    public void setOrder(Order order) {
//        this.order = order;
//    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

//    public void setOrderStatus(OrderStatus orderStatus) {
//        this.orderStatus = orderStatus;
//    }

    public String getCustomerUsername() {
        return customerUsername;
    }

//    public void setCustomerUsername(String customerUsername) {
//        this.customerUsername = customerUsername;
//    }

    public String getLogisticsOperatorUsername() {
        return logisticsOperatorUsername;
    }

//    public void setLogisticsOperatorUsername(String logisticsOperatorUsername) {
//        this.logisticsOperatorUsername = logisticsOperatorUsername;
//    }
}