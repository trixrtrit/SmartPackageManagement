package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.ws;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.mail.MessagingException;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.assemblers.CustomerAssembler;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.assemblers.OrderAssembler;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.dtos.CustomerDTO;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.dtos.OrderDTO;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.ejbs.CustomerBean;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.ejbs.OrderBean;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.dtos.EmailDTO;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.ejbs.EmailBean;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.Customer;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.enums.OrderStatus;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.pagination.PaginationMetadata;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.pagination.PaginationResponse;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.security.Authenticated;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.specifications.GenericFilterMapBuilder;

import java.util.HashMap;
import java.util.Map;

@Path("customers")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class CustomerService {
    @EJB
    private CustomerBean customerBean;
    @EJB
    private EmailBean emailBean;
    @Context
    private SecurityContext securityContext;

    @EJB
    private OrderBean orderBean;

    @GET
    @Path("/all")
    @Authenticated
    @RolesAllowed({"LogisticsOperator"})
    public Response getAll(@QueryParam("username") String username,
                           @QueryParam("name") String name,
                           @QueryParam("email") String email,
                           @QueryParam("nif") String nif,
                           @QueryParam("address") String address,
                           @DefaultValue("1") @QueryParam("page") int page,
                           @DefaultValue("10") @QueryParam("pageSize") int pageSize
    ) throws IllegalArgumentException {

        Map<String, String> filterMap = new HashMap<>();
        GenericFilterMapBuilder.addToFilterMap(username, filterMap, "username", "");
        GenericFilterMapBuilder.addToFilterMap(name, filterMap, "name", "");
        GenericFilterMapBuilder.addToFilterMap(email, filterMap, "email", "");
        GenericFilterMapBuilder.addToFilterMap(nif, filterMap, "nif", "");
        GenericFilterMapBuilder.addToFilterMap(address, filterMap, "address", "");

        var dtos = CustomerAssembler.from(customerBean.getCustomers(filterMap, page, pageSize));
        long totalItems = customerBean.getCustomersCount(filterMap);
        long totalPages = (totalItems + pageSize - 1) / pageSize;
        PaginationMetadata paginationMetadata = new PaginationMetadata(page, pageSize, totalItems, totalPages, dtos.size());
        PaginationResponse<CustomerDTO> paginationResponse = new PaginationResponse<>(dtos, paginationMetadata);
        return Response.ok(paginationResponse).build();
    }

    @GET
    @Path("{username}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    @Authenticated
    @RolesAllowed({"Customer", "LogisticsOperator"})
    public Response get(@PathParam("username") String username) throws MyEntityNotFoundException {
        var principal = securityContext.getUserPrincipal();

        if (securityContext.isUserInRole("Customer") && !principal.getName().equals(username)) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        Customer customer = customerBean.find(username);

        if (customer != null) {
            return Response.ok(CustomerAssembler.fromWithOrders(customer)).build();
        }
        return Response.status(Response.Status.NOT_FOUND)
                .entity("ERROR_FINDING_CUSTOMER")
                .build();
    }

    @GET
    @Path("{username}/orders")
    @Authenticated
    @RolesAllowed({"LogisticsOperator"})
    public Response getCustomerOrders(
            @PathParam("username") String username,
            @QueryParam("status") OrderStatus orderStatus,
            @DefaultValue("1") @QueryParam("page") int page,
            @DefaultValue("10") @QueryParam("pageSize") int pageSize) {
        Map<String, String> filterMap = new HashMap<>();
        GenericFilterMapBuilder.addToFilterMap(orderStatus, filterMap, "status", "equal");
        filterMap.put("Join/_/customer/_/username/_/equal", username);
        var orders = orderBean.getOrders(filterMap, page, pageSize);
        var dtos = OrderAssembler.fromNoOrderItems(orders);
        long totalItems = orderBean.getOrdersCount(filterMap);
        long totalPages = (totalItems + pageSize - 1) / pageSize;
        PaginationMetadata paginationMetadata = new PaginationMetadata(page, pageSize, totalItems, totalPages, dtos.size());
        PaginationResponse<OrderDTO> paginationResponse = new PaginationResponse<>(dtos, paginationMetadata);
        return Response.ok(paginationResponse).build();
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
        return Response.status(Response.Status.CREATED).entity(CustomerAssembler.from(customer)).build();
    }

    @PUT
    @Path("{username}")
    @Authenticated
    @RolesAllowed({"Customer"})
    public Response update(@PathParam("username") String username, CustomerDTO customerDTO) throws MyEntityNotFoundException {
        var principal = securityContext.getUserPrincipal();
        if (!principal.getName().equals(username)) {
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
        return Response.ok(CustomerAssembler.from(customer)).build();
    }

    @DELETE
    @Path("{username}")
    @Authenticated
    @RolesAllowed({"Customer", "LogisticsOperator"})
    public Response delete(@PathParam("username") String username) throws MyEntityNotFoundException {
        var principal = securityContext.getUserPrincipal();
        if (!principal.getName().equals(username)) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        Customer customer = customerBean.delete(username);
        return Response.status(Response.Status.OK).entity(CustomerAssembler.from(customer)).build();
    }

    @POST
    @Path("/{username}/email/send")
    @Authenticated
    @RolesAllowed({"LogisticsOperator"})
    public Response sendEmail(@PathParam("username") String username, EmailDTO email)
            throws MyEntityNotFoundException, MessagingException {
        Customer customer = customerBean.find(username);
        if (customer == null) {
            throw new MyEntityNotFoundException("Customer with username '" + username
                    + "' not found in our records.");
        }
        emailBean.send(customer.getEmail(), email.getSubject(), email.getMessage());
        return Response.status(Response.Status.OK).entity("E-mail sent").build();
    }
}
