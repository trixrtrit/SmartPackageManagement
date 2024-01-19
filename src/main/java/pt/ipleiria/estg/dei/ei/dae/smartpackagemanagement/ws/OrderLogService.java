package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.ws;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.assemblers.OrderLogAssembler;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.dtos.OrderLogDTO;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.ejbs.OrderLogBean;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.enums.OrderStatus;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.security.Authenticated;

import java.time.Instant;
import java.time.format.DateTimeParseException;

@Path("orderlogs")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class OrderLogService {

    @EJB
    private OrderLogBean orderLogBean;

    @Context
    private SecurityContext securityContext;

    @GET
    @Path("/all")
    @Authenticated
    @RolesAllowed({"LogisticsOperator"})
    public Response getAll(@QueryParam("orderId") Long orderId,
                           @QueryParam("startDate") String startDateStr,
                           @QueryParam("endDate") String endDateStr,
                           @QueryParam("status") OrderStatus status,
                           @QueryParam("customerUsername") String customerUsername,
                           @QueryParam("logisticsOperatorUsername") String logisticsOperatorUsername
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
        var dtos = OrderLogAssembler.from(orderLogBean.getOrderLogs(orderId, startDate, endDate, status, customerUsername, logisticsOperatorUsername));
        return Response.ok(dtos).build();
    }

    @POST
    @Path("/")
    @Authenticated
    @RolesAllowed({"LogisticsOperator"})
    public Response create(OrderLogDTO orderLogDTO)
            throws MyEntityExistsException, MyEntityNotFoundException, MyConstraintViolationException {
        long orderLogId = orderLogBean.create(
                orderLogDTO.getLogEntry(),
                orderLogDTO.getOrderId(),
                orderLogDTO.getOrderStatus(),
                orderLogDTO.getLogisticsOperatorUsername()
        );
        var orderLog = orderLogBean.find(orderLogId);
        return Response.status(Response.Status.CREATED).entity(OrderLogAssembler.from(orderLog)).build();
    }
}