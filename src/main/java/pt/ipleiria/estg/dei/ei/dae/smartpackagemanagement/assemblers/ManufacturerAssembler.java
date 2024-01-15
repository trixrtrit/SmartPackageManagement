package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.assemblers;

import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.dtos.ManufacturerDTO;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.dtos.ProductDTO;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.Manufacturer;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.Product;

import java.util.List;
import java.util.stream.Collectors;

public class ManufacturerAssembler {
    public static ManufacturerDTO fromWithProducts(Manufacturer manufacturer) {
        return new ManufacturerDTO(
                manufacturer.getUsername(),
                manufacturer.getPassword(),
                manufacturer.getEmail(),
                manufacturer.getName(),
                ProductAssembler.from(manufacturer.getProducts())
        );
    }

    public static List<ManufacturerDTO> fromWithProducts(List<Manufacturer> manufacturers) {
        return manufacturers.stream().map(ManufacturerAssembler::fromWithProducts).collect(Collectors.toList());
    }

    public static ManufacturerDTO from(Manufacturer manufacturer) {
        return new ManufacturerDTO(
                manufacturer.getUsername(),
                manufacturer.getPassword(),
                manufacturer.getEmail(),
                manufacturer.getName()
        );
    }

    public static List<ManufacturerDTO> from(List<Manufacturer> manufacturers) {
        return manufacturers.stream().map(ManufacturerAssembler::from).collect(Collectors.toList());
    }

}
