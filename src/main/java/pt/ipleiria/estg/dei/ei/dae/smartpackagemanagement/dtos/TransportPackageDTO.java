package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.dtos;

import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.enums.PackageType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TransportPackageDTO extends PackageDTO implements Serializable {
    private List<TransportPackageStandardPackagesDTO> transportPackageStandardPackagesMetadata;

    public TransportPackageDTO() {
        this.transportPackageStandardPackagesMetadata = new ArrayList<>();
    }

    public TransportPackageDTO(long code, String material, boolean isActive) {
        super(code, material, isActive);
    }

    public TransportPackageDTO(long code, String material, boolean isActive, List<SensorPackageDTO> sensorPackageMetadata) {
        super(code, material, isActive, sensorPackageMetadata);
        this.transportPackageStandardPackagesMetadata = new ArrayList<>();
    }

    public TransportPackageDTO(
            long code,
            String material,
            boolean isActive,
            List<TransportPackageStandardPackagesDTO> transportPackageStandardPackagesMetadata,
            boolean hasSensors
    ) {
        super(code, material, isActive);
        this.transportPackageStandardPackagesMetadata = new ArrayList<>();
    }

    public List<TransportPackageStandardPackagesDTO> getTransportPackageStandardPackagesMetadata() {
        return transportPackageStandardPackagesMetadata;
    }

    public void setTransportPackageStandardPackagesMetadata(List<TransportPackageStandardPackagesDTO> transportPackageStandardPackagesMetadata) {
        this.transportPackageStandardPackagesMetadata = transportPackageStandardPackagesMetadata;
    }
}
