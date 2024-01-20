package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.dtos;

import java.io.Serializable;

public class TopProductDTO implements Serializable {

    private String productReference;
    private String productName;
    private long totalOrders;

    public TopProductDTO(String productReference, String productName, long totalOrders) {
        this.productReference = productReference;
        this.productName = productName;
        this.totalOrders = totalOrders;
    }

    public String getProductReference() {
        return productReference;
    }

    public String getProductName() {
        return productName;
    }

    public long getTotalOrders() {
        return totalOrders;
    }
}
