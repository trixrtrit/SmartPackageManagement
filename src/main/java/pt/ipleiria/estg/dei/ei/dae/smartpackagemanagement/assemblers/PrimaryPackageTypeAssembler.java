package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.assemblers;

import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.dtos.PrimaryPackageMeasurementUnitDTO;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.dtos.PrimaryPackageTypeDTO;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.PrimaryPackageMeasurementUnit;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.PrimaryPackageType;

import java.util.List;
import java.util.stream.Collectors;

public class PrimaryPackageTypeAssembler {

    public static PrimaryPackageTypeDTO from(PrimaryPackageType primaryPackageType){
        return new PrimaryPackageTypeDTO(
                primaryPackageType.getId(),
                primaryPackageType.getType()
        );
    }

    public static List<PrimaryPackageTypeDTO> from(List<PrimaryPackageType> primaryPackageTypes) {
        return primaryPackageTypes.stream().map(PrimaryPackageTypeAssembler::from).collect(Collectors.toList());
    }
}
