package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.assemblers;

import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.dtos.MeasurementDTO;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.Measurement;

import java.util.List;
import java.util.stream.Collectors;

public class MeasurementAssembler {
    public static MeasurementDTO from (Measurement measurement){
        return new MeasurementDTO(
                measurement.getMeasurement(),
                measurement.getTimestamp(),
                SensorPackageAssembler.fromWithPackageAndSensor(measurement.getSensorPackage())
        );
    }

    public static List<MeasurementDTO> from(List<Measurement> measurements) {
        return measurements.stream().map(MeasurementAssembler::from).collect(Collectors.toList());
    }

    public static MeasurementDTO fromWithPackages (Measurement measurement){
        return new MeasurementDTO(
                measurement.getMeasurement(),
                measurement.getTimestamp(),
                SensorPackageAssembler.fromWithPackages(measurement.getSensorPackage())
        );
    }
    public static List<MeasurementDTO> fromWithPackages(List<Measurement> measurements) {
        return measurements.stream().map(MeasurementAssembler::fromWithPackages).collect(Collectors.toList());
    }
}
