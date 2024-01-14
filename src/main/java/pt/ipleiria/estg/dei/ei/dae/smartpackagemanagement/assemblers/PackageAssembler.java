package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.assemblers;

import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.dtos.PackageDTO;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.Package;

import java.util.List;
import java.util.stream.Collectors;

public class PackageAssembler {

    public static PackageDTO from(Package aPackage) {
        return new PackageDTO(
                aPackage.getCode(),
                aPackage.getMaterial(),
                aPackage.getPackageType(),
                aPackage.isActive()
        );
    }

    public static List<PackageDTO> from(List<Package> aPackages) {
        return aPackages.stream().map(PackageAssembler::from).collect(Collectors.toList());
    }

    public static PackageDTO fromWithProducts(Package aPackage) {
        return new PackageDTO(
                aPackage.getCode(),
                aPackage.getMaterial(),
                aPackage.getPackageType(),
                aPackage.isActive(),
                ProductAssembler.from(aPackage.getProducts())
        );
    }

    public static List<PackageDTO> fromWithProducts(List<Package> aPackages) {
        return aPackages.stream().map(PackageAssembler::fromWithProducts).collect(Collectors.toList());
    }

    public static PackageDTO fromWithSensors(Package aPackage) {
        return new PackageDTO(
                aPackage.getCode(),
                aPackage.getMaterial(),
                aPackage.getPackageType(),
                aPackage.isActive(),
                SensorPackageAssembler.from(aPackage.getSensorPackageList()),
                true
        );
    }

    public static List<PackageDTO> fromWithSensors(List<Package> aPackages) {
        return aPackages.stream().map(PackageAssembler::fromWithSensors).collect(Collectors.toList());
    }
}
