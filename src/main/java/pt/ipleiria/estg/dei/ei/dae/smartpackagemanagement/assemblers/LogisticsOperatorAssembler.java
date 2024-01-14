package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.assemblers;

import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.dtos.LogisticsOperatorDTO;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.LogisticsOperator;

import java.util.List;
import java.util.stream.Collectors;

public class LogisticsOperatorAssembler {
    public static LogisticsOperatorDTO from(LogisticsOperator logisticsOperator) {
        return new LogisticsOperatorDTO(
                logisticsOperator.getUsername(),
                logisticsOperator.getPassword(),
                logisticsOperator.getEmail(),
                logisticsOperator.getName()
        );
    }

    public static List<LogisticsOperatorDTO> from(List<LogisticsOperator> logisticsOperators) {
        return logisticsOperators.stream().map(LogisticsOperatorAssembler::from).collect(Collectors.toList());
    }
}
