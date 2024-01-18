package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.ws;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.assemblers.DeliveryAssembler;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.assemblers.OrderAssembler;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.dtos.OrderDTO;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.dtos.OrderItemDTO;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.ejbs.OrderBean;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.OrderItem;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.enums.OrderStatus;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.*;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.pagination.PaginationMetadata;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.pagination.PaginationResponse;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.security.Authenticated;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.specifications.GenericFilterMapBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    public Response getOrders(
                              @QueryParam("status") OrderStatus orderStatus,
                              @DefaultValue("1") @QueryParam("page") int page,
                              @DefaultValue("10") @QueryParam("pageSize") int pageSize) {
        Map<String, String> filterMap = new HashMap<>();
        GenericFilterMapBuilder.addToFilterMap(orderStatus, filterMap, "status", "");
        var orders = orderBean.getOrders(filterMap, page, pageSize);
        var dtos = OrderAssembler.fromNoOrderItems(orders);
        long totalItems = orderBean.getOrdersCount(filterMap);
        long totalPages = (totalItems + pageSize - 1) / pageSize;
        PaginationMetadata paginationMetadata = new PaginationMetadata(page, pageSize, totalItems, totalPages, dtos.size());
        PaginationResponse<OrderDTO> paginationResponse = new PaginationResponse<>(dtos, paginationMetadata);
        return Response.ok(paginationResponse).build();
    }

    @GET
    @Path("/my")
    @Authenticated
    @RolesAllowed({"Customer"})
    public Response getMyOrders(
            @QueryParam("status") OrderStatus orderStatus,
            @DefaultValue("1") @QueryParam("page") int page,
            @DefaultValue("10") @QueryParam("pageSize") int pageSize) {
        var principal = securityContext.getUserPrincipal();
        Map<String, String> filterMap = new HashMap<>();
        System.out.println(orderStatus);
        GenericFilterMapBuilder.addToFilterMap(orderStatus, filterMap, "status", "equal");
        filterMap.put("Join/_/customer/_/username/_/equal", principal.getName());
        var orders = orderBean.getOrders(filterMap, page, pageSize);
        var dtos = OrderAssembler.fromNoOrderItems(orders);
        long totalItems = orderBean.getOrdersCount(filterMap);
        long totalPages = (totalItems + pageSize - 1) / pageSize;
        PaginationMetadata paginationMetadata = new PaginationMetadata(page, pageSize, totalItems, totalPages, dtos.size());
        PaginationResponse<OrderDTO> paginationResponse = new PaginationResponse<>(dtos, paginationMetadata);
        return Response.ok(paginationResponse).build();
    }

    @GET
    @Path("{id}")
    @Authenticated
    @RolesAllowed({"Customer", "LogisticsOperator"})
    public Response getOrder(@PathParam("id") long id) throws MyEntityNotFoundException {
        var order = orderBean.find(id);
        return Response.status(Response.Status.OK).entity(OrderAssembler.from(order)).build();
    }

    @GET
    @Path("{id}/deliveries")
    @Authenticated
    @RolesAllowed({"Customer", "LogisticsOperator"})
    public Response getOrderDeliveries(@PathParam("id") long id) throws MyEntityNotFoundException {
        var order = orderBean.findWithDeliveries(id);
        return Response.status(Response.Status.OK).entity(DeliveryAssembler.from(order.getDeliveries())).build();
    }

    @PATCH
    @Path("{id}/update-status")
    @Authenticated
    @RolesAllowed({"LogisticsOperator"})
    public Response updateStatus(@PathParam("id") Long id, OrderStatus orderStatus) throws MyEntityNotFoundException, MyIllegalConstraintException, MyConstraintViolationException {
        orderBean.updateStatus(id, orderStatus);
        return Response.status(Response.Status.OK).build();
    }
}
