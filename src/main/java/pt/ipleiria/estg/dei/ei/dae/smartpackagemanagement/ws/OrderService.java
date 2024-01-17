package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.ws;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.assemblers.OrderAssembler;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.dtos.OrderDTO;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.dtos.OrderItemDTO;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.ejbs.OrderBean;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.OrderItem;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.enums.OrderStatus;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyValidationException;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.security.Authenticated;

import java.util.List;
import java.util.stream.Collectors;

@Path("orders")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class OrderService {
    @EJB
    private OrderBean orderBean;

    @Context
    private SecurityContext securityContext;

    private OrderItem orderItemDTOToEntity(OrderItemDTO orderItemDTO){
        return new OrderItem(
                orderItemDTO.getQuantity(),
                orderItemDTO.getPrice(),
                orderItemDTO.getPackageType(),
                orderItemDTO.getProductId()
        );
    }

    private List<OrderItem> orderItemDTOsToEntities(List<OrderItemDTO> orderItemDTOs){
        return orderItemDTOs.stream().map(this::orderItemDTOToEntity).collect(Collectors.toList());
    }

    @POST
    @Path("/")
    @Authenticated
    @RolesAllowed({"Customer"})
    public Response create(OrderDTO orderDTO)
            throws MyEntityExistsException, MyEntityNotFoundException, MyConstraintViolationException, MyValidationException {

        var username = securityContext.getUserPrincipal().getName();

        var id = orderBean.create(
                orderDTO.getAddress(),
                orderDTO.getPhoneNumber(),
                orderDTO.getPostCode(),
                orderDTO.getCity(),
                orderDTO.getTotalPrice(),
                orderDTO.getDate(),
                username,
                orderItemDTOsToEntities(orderDTO.getOrderItems())
        );

        var order = orderBean.find(id);

        return Response.status(Response.Status.CREATED).entity(OrderAssembler.fromNoOrderItems(order)).build();
    }

    @GET
    @Path("/all")
    @Authenticated
    @RolesAllowed({"LogisticsOperator"})
    public Response getOrders() {
        var orders = orderBean.getOrders();
        return Response.status(Response.Status.CREATED).entity(OrderAssembler.fromNoOrderItems(orders)).build();
    }

    @GET
    @Path("{id}")
    @Authenticated
    @RolesAllowed({"Customer", "LogisticsOperator"})
    public Response getOrder(@PathParam("id") long id) throws MyEntityNotFoundException {
        var order = orderBean.find(id);

        //não faço a mínima ideia se isto tá correto mas ok
        for (var orderItem : order.getOrderItems()){
            Hibernate.initialize(orderItem.getProduct());
            Hibernate.initialize(orderItem.getProduct().getPrimaryPackageMeasurementUnit());
            Hibernate.initialize(orderItem.getProduct().getPrimaryPackageType());
            Hibernate.initialize(orderItem.getProduct().getProductCategory());
        }

        return Response.status(Response.Status.CREATED).entity(OrderAssembler.from(order)).build();
    }

    @PATCH
    @Path("{id}/update-status")
    @Authenticated
    @RolesAllowed({"LogisticsOperator"}) //como é que se vai fazer update automático??
    public Response updateStatus(@PathParam("id") Long id, OrderStatus orderStatus) throws MyEntityNotFoundException, MyValidationException {
        orderBean.updateStatus(id, orderStatus);
        return Response.status(Response.Status.OK).build();
    }
}
