package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.dtos;

import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotBlank;

import java.time.Instant;

public class OrderLogDTO {
    private String logEntry;
    @Temporal(TemporalType.TIMESTAMP)
    private Instant timestamp;
    @NotBlank
    private long orderId;

    public OrderLogDTO() {
    }
    public OrderLogDTO(String logEntry, Instant timestamp, long orderId) {
        this.logEntry = logEntry;
        this.timestamp = timestamp;
        this.orderId = orderId;
    }

    public String getLogEntry() {
        return logEntry;
    }

    public void setLogEntry(String logEntry) {
        this.logEntry = logEntry;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }
}
