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
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.ejbs.CustomerBean;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.Customer;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.Order;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.security.Authenticated;

import java.util.List;
import java.util.stream.Collectors;

@Path("customers")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class CustomerService {
    @EJB
    private CustomerBean customerBean;

    @Context
    private SecurityContext securityContext;

    private CustomerDTO toDTO(Customer customer) {
        return new CustomerDTO(
                customer.getUsername(),
                customer.getPassword(),
                customer.getEmail(),
                customer.getName(),
                customer.getNif(),
                customer.getAddress(),
                ordersToDTOs(customer.getOrders())
        );
    }

    private CustomerDTO toDTOnoOrders(Customer customer) {
        return new CustomerDTO(
                customer.getUsername(),
                customer.getPassword(),
                customer.getEmail(),
                customer.getName(),
                customer.getNif(),
                customer.getAddress()
        );
    }

    private OrderDTO orderToDTO(Order order) {
        return new OrderDTO(
                order.getId(),
                order.getAddress(),
                order.getTotalPrice(),
                order.getDate(),
                order.getStatus()
        );
    }

    private List<CustomerDTO> toDTOs(List<Customer> customers) {
        return customers.stream().map(this::toDTO).collect(Collectors.toList());
    }

    private List<CustomerDTO> toDTOsNoOrders(List<Customer> customers) {
        return customers.stream().map(this::toDTOnoOrders).collect(Collectors.toList());
    }

    private List<OrderDTO> ordersToDTOs(List<Order> orders) {
        return orders.stream().map(this::orderToDTO).collect(Collectors.toList());
    }

    @GET
    @Path("/all")
    @RolesAllowed({"LogisticsOperator"})
    public List<CustomerDTO> getAll() {
        return toDTOsNoOrders(customerBean.getCustomers());
    }

    @GET
    @Path("{username}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    @Authenticated
    @RolesAllowed({"Customer"})
    public Response get(@PathParam("username") String username) throws MyEntityNotFoundException {
        var principal = securityContext.getUserPrincipal();

        if (!principal.getName().equals(username)){
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        Customer customer = customerBean.find(username);

        if (customer != null) {
            return Response.ok(toDTO(customer)).build();
        }
        return Response.status(Response.Status.NOT_FOUND)
                .entity("ERROR_FINDING_CUSTOMER")
                .build();
    }

    @GET
    @Path("{username}/orders")
    @Authenticated
    @RolesAllowed({"Customer, LogisticsOperator"})
    public Response getCustomerOrders(@PathParam("username") String username) throws MyEntityNotFoundException{
        var principal = securityContext.getUserPrincipal();
        if (!principal.getName().equals(username)){
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        Customer customer = customerBean.getCustomerOrders(username);
        if (customer != null) {
            var dtos = ordersToDTOs(customer.getOrders());
            return Response.ok(dtos).build();
        }
        return Response.status(Response.Status.NOT_FOUND)
                .entity("ERROR_FINDING_CUSTOMER")
                .build();
    }

    @POST
    @Path("/")
    public Response create(CustomerDTO customerDTO)
            throws MyEntityExistsException, MyEntityNotFoundException, MyConstraintViolationException {
        customerBean.create(
                customerDTO.getUsername(),
                customerDTO.getPassword(),
                customerDTO.getName(),
                customerDTO.getEmail(),
                customerDTO.getNif(),
                customerDTO.getAddress()
        );
        var customer = customerBean.find(customerDTO.getUsername());
        return Response.status(Response.Status.CREATED).entity(toDTOnoOrders(customer)).build();
    }

    @PUT
    @Path("{username}")
    @Authenticated
    @RolesAllowed({"Customer"})
    public Response update(@PathParam("username") String username, CustomerDTO customerDTO) throws MyEntityNotFoundException {
        var principal = securityContext.getUserPrincipal();
        if (!principal.getName().equals(username)){
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        customerBean.update(
                username,
                customerDTO.getName(),
                customerDTO.getEmail(),
                customerDTO.getNif(),
                customerDTO.getAddress()
        );
        var customer = customerBean.find(username);
        return Response.ok(toDTOnoOrders(customer)).build();
    }

    @DELETE
    @Path("{username}")
    @Authenticated
    @RolesAllowed({"Customer"})
    public Response delete(@PathParam("username") String username) throws MyEntityNotFoundException{
        var principal = securityContext.getUserPrincipal();
        if (!principal.getName().equals(username)){
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        Customer customer = customerBean.delete(username);
        return Response.status(Response.Status.OK).entity(toDTO(customer)).build();
    }
}
