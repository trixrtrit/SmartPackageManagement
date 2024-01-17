package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.assemblers;

import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.dtos.SensorPackageDTO;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.Package;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.SensorPackage;

import java.util.ArrayList;
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

    public static SensorPackageDTO fromWithPackages(SensorPackage sensorPackage) {
        return new SensorPackageDTO(
                sensorPackage.getId(),
                PackageAssembler.from(sensorPackage.getaPackage()),
                sensorPackage.getAddedAt(),
                sensorPackage.getRemovedAt()
        );
    }

    public static List<SensorPackageDTO> fromWithPackages(List<SensorPackage> sensorPackages) {
        return sensorPackages.stream().map(SensorPackageAssembler::fromWithPackages).collect(Collectors.toList());
    }

    public static SensorPackageDTO fromWithPackageAndSensor(SensorPackage sensorPackage) {
        return new SensorPackageDTO(
                sensorPackage.getId(),
                PackageAssembler.fromWithProducts(sensorPackage.getaPackage()),
                SensorAssembler.from(sensorPackage.getSensor()),
                sensorPackage.getAddedAt(),
                sensorPackage.getRemovedAt()
        );
    }

    public static List<SensorPackageDTO> fromWithPackageAndSensor(List<SensorPackage> sensorPackages) {
        return sensorPackages.stream().map(SensorPackageAssembler::fromWithPackageAndSensor).collect(Collectors.toList());
    }

}
