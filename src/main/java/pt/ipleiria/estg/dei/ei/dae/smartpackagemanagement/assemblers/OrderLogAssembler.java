package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.assemblers;

import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.dtos.OrderLogDTO;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.OrderLog;

import java.util.List;
import java.util.stream.Collectors;

public class OrderLogAssembler {
    public static OrderLogDTO from(OrderLog orderLog){
        return new OrderLogDTO(
                orderLog.getLogEntry(),
                orderLog.getTimestamp(),
                orderLog.getOrder().getId(),
                orderLog.getOrderStatus(),
                orderLog.getCustomerUsername(),
                orderLog.getLogisticsOperatorUsername()
        );
    }

    public static List<OrderLogDTO> from(List<OrderLog> orderLogs) {
        return orderLogs.stream().map(OrderLogAssembler::from).collect(Collectors.toList());
    }
}
