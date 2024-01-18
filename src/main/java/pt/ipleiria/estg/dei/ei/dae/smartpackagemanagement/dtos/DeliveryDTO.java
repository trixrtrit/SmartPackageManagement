package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.enums.DeliveryStatus;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;


public class DeliveryDTO  implements Serializable {
    private Long id;

    @NotNull
    private Instant dispatchedDate;
    private Instant deliveredDate;

    @NotNull
    private DeliveryStatus status;

    @Positive
    private Long orderId;

    @NotNull
    @Min(1)
    private ArrayList<Long> packageCodes;
    private List<PackageDTO> packages;

    public DeliveryDTO() {
    }

    public DeliveryDTO(Long id, Instant dispatchedDate, Instant deliveredDate, DeliveryStatus status, Long orderId) {
        this(id, dispatchedDate, deliveredDate, status, orderId, new ArrayList<>());
    }

    public DeliveryDTO(Long id, Instant dispatchedDate, Instant deliveredDate, DeliveryStatus status, Long orderId, List<PackageDTO> packages) {
        this.id = id;
        this.dispatchedDate = dispatchedDate;
        this.deliveredDate = deliveredDate;
        this.status = status;
        this.orderId = orderId;
        this.packages = packages;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDispatchedDate() {
        return dispatchedDate;
    }

    public void setDispatchedDate(Instant dispatchedDate) {
        this.dispatchedDate = dispatchedDate;
    }

    public Instant getDeliveredDate() {
        return deliveredDate;
    }

    public void setDeliveredDate(Instant deliveredDate) {
        this.deliveredDate = deliveredDate;
    }

    public DeliveryStatus getStatus() {
        return status;
    }

    public void setStatus(DeliveryStatus status) {
        this.status = status;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public ArrayList<Long> getPackageCodes() {
        return packageCodes;
    }

    public void setPackageCodes(ArrayList<Long> packageCodes) {
        this.packageCodes = packageCodes;
    }

    public List<PackageDTO> getPackages() {
        return packages;
    }

    public void setPackages(List<PackageDTO> packages) {
        this.packages = packages;
    }
}
