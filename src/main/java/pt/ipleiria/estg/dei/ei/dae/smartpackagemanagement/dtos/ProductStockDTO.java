package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.dtos;

import jakarta.validation.constraints.PositiveOrZero;

public class ProductStockDTO {
    @PositiveOrZero
    private float unitStock;
    @PositiveOrZero
    private float boxStock;
    @PositiveOrZero
    private float containerStock;

    public ProductStockDTO() {
    }

    public ProductStockDTO(float unitStock, float boxStock, float containerStock) {
        this.unitStock = unitStock;
        this.boxStock = boxStock;
        this.containerStock = containerStock;
    }

    public float getUnitStock() {
        return unitStock;
    }

    public void setUnitStock(float unitStock) {
        this.unitStock = unitStock;
    }

    public float getBoxStock() {
        return boxStock;
    }

    public void setBoxStock(float boxStock) {
        this.boxStock = boxStock;
    }

    public float getContainerStock() {
        return containerStock;
    }

    public void setContainerStock(float containerStock) {
        this.containerStock = containerStock;
    }
}
