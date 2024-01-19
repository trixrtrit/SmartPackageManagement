package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.assemblers;

import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.dtos.SensorDTO;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.Measurement;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.Package;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.Sensor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SensorAssembler {

    public static SensorDTO from(Sensor sensor){
        return new SensorDTO(
                sensor.getId(),
                sensor.getName(),
                sensor.isAvailable(),
                SensorTypeAssembler.from(sensor.getSensorType())
        );
    }

    public static List<SensorDTO> from(List<Sensor> sensors) {
        return sensors.stream().map(SensorAssembler::from).collect(Collectors.toList());
    }

    public static SensorDTO fromWithMeasurements(Sensor sensor, List<Measurement> measurements){
        return new SensorDTO(
                sensor.getId(),
                sensor.getName(),
                sensor.isAvailable(),
                SensorTypeAssembler.from(sensor.getSensorType()),
                MeasurementAssembler.fromWithPackages(measurements)
        );
    }
}
