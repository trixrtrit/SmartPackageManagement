package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.assemblers;

import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.dtos.OrderItemDTO;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.OrderItem;

import java.util.List;
import java.util.stream.Collectors;

public class OrderItemAssembler {
    public static OrderItemDTO from(OrderItem orderItem) {
        return new OrderItemDTO(
                orderItem.getId(),
                orderItem.getQuantity(),
                orderItem.getPrice(),
                orderItem.getOrder().getId(),
                orderItem.getPackageType(),
                ProductAssembler.from(orderItem.getProduct())
        );
    }

    public static List<OrderItemDTO> from(List<OrderItem> orderItems) {
        return orderItems.stream().map(OrderItemAssembler::from).collect(Collectors.toList());
    }
}
