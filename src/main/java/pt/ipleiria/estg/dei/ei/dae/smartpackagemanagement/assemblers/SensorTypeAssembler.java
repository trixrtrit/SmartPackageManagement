package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.assemblers;

import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.dtos.SensorTypeDTO;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.SensorType;

import java.util.List;
import java.util.stream.Collectors;

public class SensorTypeAssembler {
    public static SensorTypeDTO from(SensorType sensorType) {
        return new SensorTypeDTO(
                sensorType.getId(),
                sensorType.getName(),
                sensorType.getMeasurementUnit()
        );
    }

    public static List<SensorTypeDTO> from(List<SensorType> sensorTypes) {
        return sensorTypes.stream().map(SensorTypeAssembler::from).collect(Collectors.toList());
    }

}
