package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.dtos;

import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotBlank;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.enums.OrderStatus;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.serialization.EnumCustomSerialization;


import java.io.Serializable;
import java.time.Instant;
import java.util.Date;

public class OrderLogDTO  implements Serializable {
    private String logEntry;
    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;
    @NotBlank
    private long orderId;
    private String orderStatus;
    private String customerUsername;
    private String logisticsOperatorUsername;

    public OrderLogDTO() {
    }
    public OrderLogDTO(String logEntry, Date timestamp, long orderId, String newStatus, String customerUsername, String logisticsOperatorUsername) {
        this.logEntry = logEntry;
        this.timestamp = timestamp;
        this.orderId = orderId;
        this.orderStatus = newStatus;
        this.customerUsername = customerUsername;
        this.logisticsOperatorUsername = logisticsOperatorUsername;
    }

    public String getLogEntry() {
        return logEntry;
    }

//    public void setLogEntry(String logEntry) {
//        this.logEntry = logEntry;
//    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public long getOrderId() {
        return orderId;
    }

//    public void setOrderId(long orderId) {
//        this.orderId = orderId;
//    }
public String getOrderStatus() {
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
