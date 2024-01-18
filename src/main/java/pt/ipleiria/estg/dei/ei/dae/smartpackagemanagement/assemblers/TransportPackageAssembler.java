package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.assemblers;

import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.dtos.TransportPackageDTO;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.TransportPackage;

import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

public class TransportPackageAssembler {
    public static TransportPackageDTO from(TransportPackage transportPackage) {
        return new TransportPackageDTO(
                transportPackage.getCode(),
                transportPackage.getMaterial(),
                transportPackage.isActive(),
                transportPackage.getManufactureDate()
        );
    }

    public static List<TransportPackageDTO> from(List<TransportPackage> transportPackages) {
        return transportPackages.stream().map(TransportPackageAssembler::from).collect(Collectors.toList());
    }

    public static TransportPackageDTO fromWithSensors(TransportPackage transportPackage) {
        return new TransportPackageDTO(
                transportPackage.getCode(),
                transportPackage.getMaterial(),
                transportPackage.isActive(),
                transportPackage.getManufactureDate(),
                SensorPackageAssembler.from(transportPackage.getSensorPackageList())
        );
    }

    public static List<TransportPackageDTO> fromWithSensors(List<TransportPackage> transportPackages) {
        return transportPackages.stream().map(TransportPackageAssembler::fromWithSensors).collect(Collectors.toList());
    }

    public static TransportPackageDTO fromWithPackages(TransportPackage transportPackage) {
        return new TransportPackageDTO(
                transportPackage.getCode(),
                transportPackage.getMaterial(),
                transportPackage.isActive(),
                transportPackage.getManufactureDate(),
                TransportPackageStandardPackagesAssembler.from(transportPackage.getTransportPackageStandardPackages()),
                false
        );
    }

    public static List<TransportPackageDTO> fromWithPackages(List<TransportPackage> transportPackages) {
        return transportPackages.stream().map(TransportPackageAssembler::fromWithPackages).collect(Collectors.toList());
    }
}
