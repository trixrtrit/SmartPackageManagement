package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.ws;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.assemblers.CustomerAssembler;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.assemblers.OrderAssembler;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.dtos.CustomerDTO;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.ejbs.CustomerBean;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.Customer;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.pagination.PaginationMetadata;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.pagination.PaginationResponse;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.security.Authenticated;

import java.util.HashMap;
import java.util.Map;

@Path("customers")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class CustomerService {
    @EJB
    private CustomerBean customerBean;

    @Context
    private SecurityContext securityContext;

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
        filterMap.put("username", username);
        filterMap.put("name", name);
        filterMap.put("email", email);
        filterMap.put("nif", nif);
        filterMap.put("address", address);

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
    @RolesAllowed({"Customer"})
    public Response get(@PathParam("username") String username) throws MyEntityNotFoundException {
        var principal = securityContext.getUserPrincipal();

        if (!principal.getName().equals(username)) {
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
    @RolesAllowed({"Customer, LogisticsOperator"})
    public Response getCustomerOrders(@PathParam("username") String username) throws MyEntityNotFoundException {
        var principal = securityContext.getUserPrincipal();
        if (!principal.getName().equals(username)) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        Customer customer = customerBean.getCustomerOrders(username);
        if (customer != null) {
            var dtos = OrderAssembler.from(customer.getOrders());
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
    @RolesAllowed({"Customer"})
    public Response delete(@PathParam("username") String username) throws MyEntityNotFoundException {
        var principal = securityContext.getUserPrincipal();
        if (!principal.getName().equals(username)) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        Customer customer = customerBean.delete(username);
        return Response.status(Response.Status.OK).entity(CustomerAssembler.from(customer)).build();
    }
}
