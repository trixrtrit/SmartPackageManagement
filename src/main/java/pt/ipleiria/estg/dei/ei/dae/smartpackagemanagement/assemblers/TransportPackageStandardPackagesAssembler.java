package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.assemblers;

import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.dtos.TransportPackageStandardPackagesDTO;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.TransportPackageStandardPackage;

import java.util.List;
import java.util.stream.Collectors;

public class TransportPackageStandardPackagesAssembler {
    public static TransportPackageStandardPackagesDTO from(
            TransportPackageStandardPackage transportPackageStandardPackage) {
        return new TransportPackageStandardPackagesDTO(
                transportPackageStandardPackage.getId(),
                TransportPackageAssembler.from(transportPackageStandardPackage.getTransportPackage()),
                StandardPackageAssembler.from(transportPackageStandardPackage.getStandardPackage()),
                transportPackageStandardPackage.getAddedAt(),
                transportPackageStandardPackage.getRemovedAt()
        );
    }

    public static List<TransportPackageStandardPackagesDTO> from(
            List<TransportPackageStandardPackage> transportPackageStandardPackages) {
        return transportPackageStandardPackages.stream().
                map(TransportPackageStandardPackagesAssembler::from).collect(Collectors.toList());
    }

    public static TransportPackageStandardPackagesDTO fromWithPackages(
            TransportPackageStandardPackage transportPackageStandardPackage) {
        return new TransportPackageStandardPackagesDTO(
                transportPackageStandardPackage.getId(),
                TransportPackageAssembler.from(transportPackageStandardPackage.getTransportPackage()),
                transportPackageStandardPackage.getAddedAt(),
                transportPackageStandardPackage.getRemovedAt()
        );
    }

    public static List<TransportPackageStandardPackagesDTO> fromWithPackages(
            List<TransportPackageStandardPackage> transportPackageStandardPackages) {
        return transportPackageStandardPackages.stream().
                map(TransportPackageStandardPackagesAssembler::fromWithPackages).collect(Collectors.toList());
    }

}
