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
                aPackage.isActive(),
                aPackage.getManufactureDate()
        );
    }

    public static List<PackageDTO> from(List<Package> aPackages) {
        return aPackages.stream().map(PackageAssembler::from).collect(Collectors.toList());
    }

    public static PackageDTO fromWithSensors(Package aPackage) {
        return new PackageDTO(
                aPackage.getCode(),
                aPackage.getMaterial(),
                aPackage.isActive(),
                aPackage.getManufactureDate(),
                SensorPackageAssembler.from(aPackage.getSensorPackageList())
        );
    }

    public static List<PackageDTO> fromWithSensors(List<Package> aPackages) {
        return aPackages.stream().map(PackageAssembler::fromWithSensors).collect(Collectors.toList());
    }
}
