package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.dtos;

import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.enums.PackageType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TransportPackageDTO extends PackageDTO implements Serializable {
    public TransportPackageDTO() {
    }

    public TransportPackageDTO(long code, String material, boolean isActive) {
        super(code, material, isActive);
    }

    public TransportPackageDTO(long code, String material, boolean isActive, List<SensorPackageDTO> sensorPackageMetadata) {
        super(code, material, isActive, sensorPackageMetadata);
    }
}
