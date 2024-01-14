package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.assemblers;

import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.dtos.ProductParameterDTO;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.ProductParameter;

import java.util.List;
import java.util.stream.Collectors;

public class ProductParameterAssembler {
    public static ProductParameterDTO from(ProductParameter productParameter) {
        return new ProductParameterDTO(
                productParameter.getId(),
                productParameter.getProduct().getId(),
                productParameter.getSensorType().getId(),
                productParameter.getMinValue(),
                productParameter.getMaxValue()
        );
    }

    public static List<ProductParameterDTO> from(List<ProductParameter> productParameters) {
        return productParameters.stream().map(ProductParameterAssembler::from).collect(Collectors.toList());
    }
}
