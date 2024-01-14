package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.assemblers;

import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.dtos.ProductDTO;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.Product;

import java.util.List;
import java.util.stream.Collectors;

public class ProductAssembler {
    public static ProductDTO from(Product product) {
        return new ProductDTO(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.isActive(),
                product.getManufacturer().getUsername(),
                product.getProductReference(),
                product.getUnitStock(),
                product.getBoxStock(),
                product.getContainerStock(),
                product.getPrimaryPackQuantity(),
                product.getSecondaryPackQuantity(),
                product.getTertiaryPackQuantity()
        );
    }

    public static List<ProductDTO> from(List<Product> products) {
        return products.stream().map(ProductAssembler::from).collect(Collectors.toList());
    }
}
