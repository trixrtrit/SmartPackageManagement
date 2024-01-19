package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.dtos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TransportPackageDTO extends PackageDTO implements Serializable {
    private List<TransportPackageStandardPackagesDTO> transportPackageStandardPackagesMetadata;

    public TransportPackageDTO() {
        this.transportPackageStandardPackagesMetadata = new ArrayList<>();
    }

    public TransportPackageDTO(long code, String material, boolean isActive, Date manufactureDate) {
        super(code, material, isActive, manufactureDate);
    }

    public TransportPackageDTO(
            long code,
            String material,
            boolean isActive,
            Date manufactureDate,
            List<SensorPackageDTO> sensorPackageMetadata) {
        super(code, material, isActive, manufactureDate, sensorPackageMetadata);
        this.transportPackageStandardPackagesMetadata = new ArrayList<>();
    }

    public TransportPackageDTO(
            long code,
            String material,
            boolean isActive,
            Date manufactureDate,
            List<TransportPackageStandardPackagesDTO> transportPackageStandardPackagesMetadata,
            boolean hasSensors
    ) {
        super(code, material, isActive, manufactureDate);
        this.transportPackageStandardPackagesMetadata = transportPackageStandardPackagesMetadata;
    }

    public List<TransportPackageStandardPackagesDTO> getTransportPackageStandardPackagesMetadata() {
        return transportPackageStandardPackagesMetadata;
    }

    public void setTransportPackageStandardPackagesMetadata(List<TransportPackageStandardPackagesDTO> transportPackageStandardPackagesMetadata) {
        this.transportPackageStandardPackagesMetadata = transportPackageStandardPackagesMetadata;
    }
}
