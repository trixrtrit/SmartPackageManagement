package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.assemblers;

import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.dtos.OrderDTO;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.Order;

import java.util.List;
import java.util.stream.Collectors;

public class OrderAssembler {
    public static OrderDTO from(Order order) {
        return new OrderDTO(
                order.getId(),
                order.getAddress(),
                order.getTotalPrice(),
                order.getDate(),
                order.getStatus()
        );
    }

    public static List<OrderDTO> from(List<Order> orders) {
        return orders.stream().map(OrderAssembler::from).collect(Collectors.toList());
    }
}
