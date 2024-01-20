package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.dtos;

import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.enums.PackageType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StandardPackageDTO extends PackageDTO implements Serializable {

    private PackageType packageType;
    private List<StandardPackageProductDTO> standardPackageProductMetadata;
    public StandardPackageDTO() {
        this.standardPackageProductMetadata = new ArrayList<>();
    }
    public Long initialProductId;

    public StandardPackageDTO(long code, String material, PackageType packageType, boolean isActive, Date manufactureDate) {
        super(code, material, isActive, manufactureDate);
        this.packageType = packageType;
    }

    public StandardPackageDTO(
            long code,
            String material,
            PackageType packageType,
            boolean isActive,
            Date manufactureDate,
            List<StandardPackageProductDTO> standardPackageProductMetadata
    ) {
        super(code, material, isActive, manufactureDate);
        this.standardPackageProductMetadata = standardPackageProductMetadata;
        this.packageType = packageType;
    }

    public StandardPackageDTO(
            long code,
            String material,
            PackageType packageType,
            boolean isActive,
            Date manufactureDate,
            List<SensorPackageDTO> sensorPackageMetadata,
            boolean hasSensors
    ) {
        super(code, material, isActive, manufactureDate, sensorPackageMetadata);
        this.standardPackageProductMetadata = new ArrayList<>();
        this.packageType = packageType;
    }

    public List<StandardPackageProductDTO> getStandardPackageProductMetadata() {
        return standardPackageProductMetadata;
    }

    public void setStandardPackageProductMetadata(List<StandardPackageProductDTO> standardPackageProductMetadata) {
        this.standardPackageProductMetadata = standardPackageProductMetadata;
    }

    public PackageType getPackageType() {
        return packageType;
    }

    public void setPackageType(PackageType packageType) {
        this.packageType = packageType;
    }

    public Long getInitialProductId() {
        return initialProductId;
    }

    public void setInitialProductId(Long initialProductId) {
        this.initialProductId = initialProductId;
    }
}
