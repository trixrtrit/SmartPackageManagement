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
    private List<StandardPackageDTO> standardPackages;
    private List<TransportPackageDTO> transportPackage;

    public DeliveryDTO() {
        this.standardPackages = new ArrayList<>();
        this.transportPackage = new ArrayList<>();
        this.packageCodes = new ArrayList<>();
    }

    public DeliveryDTO(Long id, Instant dispatchedDate, Instant deliveredDate, DeliveryStatus status, Long orderId) {
        this.id = id;
        this.dispatchedDate = dispatchedDate;
        this.deliveredDate = deliveredDate;
        this.status = status;
        this.orderId = orderId;
        this.standardPackages = new ArrayList<>();
        this.transportPackage = new ArrayList<>();
        this.packageCodes = new ArrayList<>();
    }

    public DeliveryDTO(
            Long id,
            Instant dispatchedDate,
            Instant deliveredDate,
            DeliveryStatus status,
            Long orderId,
            List<StandardPackageDTO> standardPackages,
            List<TransportPackageDTO> transportPackages
    ) {
        this.id = id;
        this.dispatchedDate = dispatchedDate;
        this.deliveredDate = deliveredDate;
        this.status = status;
        this.orderId = orderId;
        this.standardPackages = standardPackages;
        this.transportPackage = transportPackages;
        this.packageCodes = new ArrayList<>();
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

    public List<StandardPackageDTO> getStandardPackages() {
        return standardPackages;
    }

    public void setStandardPackages(List<StandardPackageDTO> standardPackages) {
        this.standardPackages = standardPackages;
    }

    public List<TransportPackageDTO> getTransportPackage() {
        return transportPackage;
    }

    public void setTransportPackage(List<TransportPackageDTO> transportPackage) {
        this.transportPackage = transportPackage;
    }
}
