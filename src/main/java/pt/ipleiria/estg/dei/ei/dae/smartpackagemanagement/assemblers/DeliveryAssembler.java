package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.assemblers;

import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.dtos.DeliveryDTO;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.dtos.ProductDTO;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.Delivery;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.Package;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.Product;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.StandardPackage;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.TransportPackage;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DeliveryAssembler {
    public static DeliveryDTO from(Delivery delivery) {
        Map<Boolean, List<Package>> partitionedPackages = delivery.getPackages().stream()
                .collect(Collectors.partitioningBy(aPackage -> aPackage instanceof StandardPackage));

        List<StandardPackage> standardPackages = partitionedPackages.get(true).stream()
                .map(aPackage -> (StandardPackage) aPackage)
                .collect(Collectors.toList());

        List<TransportPackage> transportPackages = partitionedPackages.get(false).stream()
                .map(aPackage -> (TransportPackage) aPackage)
                .collect(Collectors.toList());

        return new DeliveryDTO(
                delivery.getId(),
                delivery.getDispatchedDate(),
                delivery.getDeliveredDate(),
                delivery.getStatus(),
                delivery.getOrder().getId(),
                StandardPackageAssembler.fromWithProducts(standardPackages),
                TransportPackageAssembler.fromWithPackages(transportPackages)
        );
    }

    public static List<DeliveryDTO> from(List<Delivery> deliveries) {
        return deliveries.stream().map(DeliveryAssembler::from).collect(Collectors.toList());
    }

    public static DeliveryDTO fromNoPackages(Delivery delivery) {
        return new DeliveryDTO(
                delivery.getId(),
                delivery.getDispatchedDate(),
                delivery.getDeliveredDate(),
                delivery.getStatus(),
                delivery.getOrder().getId()
        );
    }

    public static List<DeliveryDTO> fromNoPackages(List<Delivery> deliveries) {
        return deliveries.stream().map(DeliveryAssembler::fromNoPackages).collect(Collectors.toList());
    }
}
