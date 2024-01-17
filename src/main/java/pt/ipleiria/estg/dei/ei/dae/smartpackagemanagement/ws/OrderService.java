package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.ws;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.dtos.CustomerDTO;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.dtos.OrderDTO;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.dtos.OrderItemDTO;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.dtos.ProductDTO;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.ejbs.OrderBean;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.Customer;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.Order;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.OrderItem;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.Product;
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

    private OrderDTO toDTO(Order order){
        return new OrderDTO(
            order.getId(),
                order.getAddress(),
                order.getPhoneNumber(),
                order.getPostCode(),
                order.getCity(),
                order.getTotalPrice(),
                order.getDate(),
                order.getStatus(),
                customerToDTO(order.getCustomer())
        );
    }

    private List<OrderDTO> toDTOs(List<Order> orders){
        return orders.stream().map(this::toDTO).collect(Collectors.toList());
    }

    private OrderDTO orderItemToDto(Order order){
        return new OrderDTO(
                order.getId(),
                order.getAddress(),
                order.getPhoneNumber(),
                order.getPostCode(),
                order.getCity(),
                order.getTotalPrice(),
                order.getDate(),
                order.getStatus(),
                customerToDTO(order.getCustomer()),
                orderItemsToDTOS(order.getOrderItems())
        );
    }

    private List<OrderDTO> orderItemsToDtos(List<Order> orders){
        return orders.stream().map(this::orderItemToDto).collect(Collectors.toList());
    }

    private CustomerDTO customerToDTO(Customer customer){
        return new CustomerDTO(
                customer.getUsername(),
                customer.getEmail(),
                customer.getName(),
                customer.getNif(),
                customer.getAddress()
        );
    }

    private OrderItemDTO orderItemToDTO(OrderItem orderItem){
        return new OrderItemDTO(
                orderItem.getId(),
                orderItem.getQuantity(),
                orderItem.getPrice(),
                orderItem.getOrder().getId(),
                orderItem.getPackageType(),
                productToDto(orderItem.getProduct())
        );
    }

    private List<OrderItemDTO> orderItemsToDTOS(List<OrderItem> orderItems){
        return orderItems.stream().map(this::orderItemToDTO).collect(Collectors.toList());
    }

    private ProductDTO productToDto(Product product) {
        return new ProductDTO(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.isActive(),
                product.getManufacturer().getName(),
                product.getProductReference()
        );
    }

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

        return Response.status(Response.Status.CREATED).entity(orderItemToDto(order)).build();
    }

    @GET
    @Path("/all")
    @Authenticated
    @RolesAllowed({"LogisticsOperator"})
    public Response getOrders() {
        var orders = orderBean.getOrders();
        return Response.status(Response.Status.CREATED).entity(toDTOs(orders)).build();
    }

    @GET
    @Path("{id}")
    @Authenticated
    @RolesAllowed({"Customer", "LogisticsOperator"})
    public Response getOrder(@PathParam("id") long id) throws MyEntityNotFoundException {
        var order = orderBean.find(id);
        return Response.status(Response.Status.CREATED).entity(orderItemToDto(order)).build();
    }

    @PATCH
    @Path("{id}/update-status")
    @Authenticated
    @RolesAllowed({"LogisticsOperator"}) //como é que se vai fazer update automático??
    public Response updateStatus(@PathParam("id") Long id, OrderStatus orderStatus) throws MyEntityNotFoundException, MyValidationException, MyConstraintViolationException {
        orderBean.updateStatus(id, orderStatus);
        return Response.status(Response.Status.OK).build();
    }
}
