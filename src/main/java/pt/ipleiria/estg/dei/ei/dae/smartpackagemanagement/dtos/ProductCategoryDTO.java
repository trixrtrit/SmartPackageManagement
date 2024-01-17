package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.dtos;

public class ProductCategoryDTO {
    private Long id;
    public String category;
    public ProductCategoryDTO() {
    }

    public ProductCategoryDTO(Long id, String category) {
        this.id = id;
        this.category = category;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
