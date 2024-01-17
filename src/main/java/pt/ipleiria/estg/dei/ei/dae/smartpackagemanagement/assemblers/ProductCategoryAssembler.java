package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.assemblers;

import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.dtos.PrimaryPackageTypeDTO;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.dtos.ProductCategoryDTO;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.PrimaryPackageType;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.ProductCategory;

import java.util.List;
import java.util.stream.Collectors;

public class ProductCategoryAssembler {

    public static ProductCategoryDTO from(ProductCategory productCategory){
        return new ProductCategoryDTO(
                productCategory.getId(),
                productCategory.getCategory()
        );
    }

    public static List<ProductCategoryDTO> from(List<ProductCategory> productCategories) {
        return productCategories.stream().map(ProductCategoryAssembler::from).collect(Collectors.toList());
    }
}
