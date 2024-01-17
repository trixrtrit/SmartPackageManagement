package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.assemblers;

import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.dtos.PrimaryPackageMeasurementUnitDTO;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.PrimaryPackageMeasurementUnit;

import java.util.List;
import java.util.stream.Collectors;

public class PrimaryPackageMeasurementUnitAssembler {

    public static PrimaryPackageMeasurementUnitDTO from(PrimaryPackageMeasurementUnit primaryPackageMeasurementUnit){
        return new PrimaryPackageMeasurementUnitDTO(
                primaryPackageMeasurementUnit.getId(),
                primaryPackageMeasurementUnit.getUnit()
        );
    }

    public static List<PrimaryPackageMeasurementUnitDTO> from(List<PrimaryPackageMeasurementUnit> primaryPackageMeasurementUnits) {
        return primaryPackageMeasurementUnits.stream().map(PrimaryPackageMeasurementUnitAssembler::from).collect(Collectors.toList());
    }
}
