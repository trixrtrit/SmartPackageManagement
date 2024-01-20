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
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.utils.DateUtil;

import java.util.Date;

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
    @RolesAllowed({"Customer", "LogisticsOperator"})
    public Response getAll(@QueryParam("orderId") Long orderId,
                           @QueryParam("startTime") Long startTime,
                           @QueryParam("endTime") Long endTime,
                           @QueryParam("status") OrderStatus status,
                           @QueryParam("customerUsername") String customerUsername,
                           @QueryParam("logisticsOperatorUsername") String logisticsOperatorUsername
    ) {
        Date[] dates = DateUtil.parseDates(startTime, endTime);
        Date startDate = dates[0];
        Date endDate = dates[1];
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