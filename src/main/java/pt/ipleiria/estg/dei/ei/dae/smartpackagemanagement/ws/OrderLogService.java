package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.ws;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.dtos.MeasurementDTO;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.dtos.OrderLogDTO;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.ejbs.MeasurementBean;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.ejbs.OrderLogBean;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.Measurement;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.OrderLog;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.enums.OrderStatus;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.security.Authenticated;

import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

@Path("orderlogs")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class OrderLogService {

    @EJB
    private OrderLogBean orderLogBean;

    @Context
    private SecurityContext securityContext;

    private OrderLogDTO toDTO (OrderLog orderLog){
        return new OrderLogDTO(
                orderLog.getLogEntry(),
                orderLog.getTimestamp(),
                orderLog.getOrder().getId()
        );
    }

    private List<OrderLogDTO> toDTOs(List<OrderLog> orderLogs) {
        return orderLogs.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @GET
    @Path("/all")
    @Authenticated
    @RolesAllowed({"LogisticsOperator"})
    public Response getAll(@QueryParam("orderId") Long orderId,
                           @QueryParam("startDate") String startDateStr,
                           @QueryParam("endDate") String endDateStr,
                           @QueryParam("status") OrderStatus status
    ) {
        Instant startDate = null;
        Instant endDate = null;
        try {
            if(startDateStr != null) {
                startDate = Instant.parse(startDateStr);
            }
            if(endDateStr != null) {
                endDate = Instant.parse(endDateStr);
            }
        } catch ( DateTimeParseException e ) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid date format").build();
        }
        var dtos = toDTOs(orderLogBean.getOrderLogs(orderId, startDate, endDate, status));
        return Response.ok(dtos).build();
    }

    @POST
    @Path("/")
    public Response create(OrderLogDTO orderLogDTO)
            throws MyEntityExistsException, MyEntityNotFoundException, MyConstraintViolationException {
        long orderLogId = orderLogBean.create(
                orderLogDTO.getLogEntry(),
                orderLogDTO.getOrderId()
        );
        var orderLog = orderLogBean.find(orderLogId);
        return Response.status(Response.Status.CREATED).entity(toDTO(orderLog)).build();
    }













}
