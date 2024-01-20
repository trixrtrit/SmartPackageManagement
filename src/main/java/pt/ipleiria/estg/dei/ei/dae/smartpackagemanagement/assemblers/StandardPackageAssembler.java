package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.assemblers;

import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.dtos.StandardPackageDTO;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.StandardPackage;

import java.util.List;
import java.util.stream.Collectors;

public class StandardPackageAssembler {
    public static StandardPackageDTO from(StandardPackage standardPackage) {
        return new StandardPackageDTO(
                standardPackage.getCode(),
                standardPackage.getMaterial(),
                standardPackage.getPackageType(),
                standardPackage.isActive(),
                standardPackage.getManufactureDate()
        );
    }

    public static List<StandardPackageDTO> from(List<StandardPackage> standardPackages) {
        return standardPackages.stream().map(StandardPackageAssembler::from).collect(Collectors.toList());
    }

    public static StandardPackageDTO fromWithProducts(StandardPackage standardPackage) {
        return new StandardPackageDTO(
                standardPackage.getCode(),
                standardPackage.getMaterial(),
                standardPackage.getPackageType(),
                standardPackage.isActive(),
                standardPackage.getManufactureDate(),
                StandardPackageProductAssembler.from(standardPackage.getStandardPackageProducts())
        );
    }

    public static List<StandardPackageDTO> fromWithProducts(List<StandardPackage> standardPackages) {
        return standardPackages.stream().map(StandardPackageAssembler::fromWithProducts).collect(Collectors.toList());
    }

    public static StandardPackageDTO fromWithProductsAndParameters(StandardPackage standardPackage) {
        return new StandardPackageDTO(
                standardPackage.getCode(),
                standardPackage.getMaterial(),
                standardPackage.getPackageType(),
                standardPackage.isActive(),
                standardPackage.getManufactureDate(),
                StandardPackageProductAssembler.from(standardPackage.getStandardPackageProducts())
        );
    }

    public static List<StandardPackageDTO> fromWithProductsAndParameters(List<StandardPackage> standardPackages) {
        return standardPackages.stream().map(StandardPackageAssembler::fromWithProductsAndParameters).collect(Collectors.toList());
    }

    public static StandardPackageDTO fromWithSensors(StandardPackage standardPackage) {
        return new StandardPackageDTO(
                standardPackage.getCode(),
                standardPackage.getMaterial(),
                standardPackage.getPackageType(),
                standardPackage.isActive(),
                standardPackage.getManufactureDate(),
                SensorPackageAssembler.from(standardPackage.getSensorPackageList()),
                true
        );
    }

    public static List<StandardPackageDTO> fromWithSensors(List<StandardPackage> standardPackages) {
        return standardPackages.stream().map(StandardPackageAssembler::fromWithSensors).collect(Collectors.toList());
    }
}
