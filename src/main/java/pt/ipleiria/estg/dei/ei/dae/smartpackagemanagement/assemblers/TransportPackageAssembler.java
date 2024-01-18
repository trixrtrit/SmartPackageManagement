package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.assemblers;

import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.dtos.StandardPackageDTO;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.dtos.TransportPackageDTO;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.StandardPackage;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.TransportPackage;

import java.util.List;
import java.util.stream.Collectors;

public class TransportPackageAssembler {
    public static TransportPackageDTO from(TransportPackage transportPackage) {
        return new TransportPackageDTO(
                transportPackage.getCode(),
                transportPackage.getMaterial(),
                transportPackage.isActive()
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
                SensorPackageAssembler.from(transportPackage.getSensorPackageList())
        );
    }

    public static List<TransportPackageDTO> fromWithSensors(List<TransportPackage> transportPackages) {
        return transportPackages.stream().map(TransportPackageAssembler::fromWithSensors).collect(Collectors.toList());
    }
}
