package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.assemblers;

import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.dtos.DeliveryDTO;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.dtos.ProductDTO;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.Delivery;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.Product;

import java.util.List;
import java.util.stream.Collectors;

public class DeliveryAssembler {
    public static DeliveryDTO from(Delivery delivery) {
        return new DeliveryDTO(
                delivery.getId(),
                delivery.getDispatchedDate(),
                delivery.getDeliveredDate(),
                delivery.getStatus(),
                delivery.getOrder().getId(),
                PackageAssembler.fromWithProducts(delivery.getPackages())
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
