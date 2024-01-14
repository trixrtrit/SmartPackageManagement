package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.assemblers;

import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.dtos.SensorPackageDTO;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.SensorPackage;

import java.util.List;
import java.util.stream.Collectors;

public class SensorPackageAssembler {
    public static SensorPackageDTO from(SensorPackage sensorPackage) {
        return new SensorPackageDTO(
                sensorPackage.getId(),
                SensorAssembler.from(sensorPackage.getSensor()),
                sensorPackage.getAddedAt(),
                sensorPackage.getRemovedAt()
        );
    }

    public static List<SensorPackageDTO> from(List<SensorPackage> sensorPackages) {
        return sensorPackages.stream().map(SensorPackageAssembler::from).collect(Collectors.toList());
    }

    public static SensorPackageDTO fromWithMeasurements(SensorPackage sensorPackage) {
        return new SensorPackageDTO(
                sensorPackage.getId(),
                SensorAssembler.fromWithMeasurements(sensorPackage.getSensor(), sensorPackage.getMeasurements()),
                sensorPackage.getAddedAt(),
                sensorPackage.getRemovedAt()
        );
    }

    public static List<SensorPackageDTO> fromWithMeasurements(List<SensorPackage> sensorPackages) {
        return sensorPackages.stream().map(SensorPackageAssembler::fromWithMeasurements).collect(Collectors.toList());
    }
}
