package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.ws;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.mail.MessagingException;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.assemblers.LogisticsOperatorAssembler;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.dtos.EmailDTO;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.dtos.LogisticsOperatorDTO;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.dtos.ManufacturerDTO;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.ejbs.EmailBean;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.ejbs.LogisticsOperatorBean;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.LogisticsOperator;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.pagination.PaginationMetadata;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.pagination.PaginationResponse;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.security.Authenticated;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.specifications.GenericFilterMapBuilder;

import java.util.HashMap;
import java.util.Map;

@Path("logistics-operator")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class LogisticsOperatorService {

    @EJB
    private LogisticsOperatorBean logisticsOperatorBean;
    @EJB
    private EmailBean emailBean;
    @Context
    private SecurityContext securityContext;

    @GET
    @Path("/all")
    @Authenticated
    @RolesAllowed({"LogisticsOperator"})
    public Response getAll(@QueryParam("username") String username,
                           @QueryParam("name") String name,
                           @QueryParam("email") String email,
                           @DefaultValue("1") @QueryParam("page") int page,
                           @DefaultValue("10") @QueryParam("pageSize") int pageSize
    ) throws IllegalArgumentException {

        Map<String, String> filterMap = new HashMap<>();
        GenericFilterMapBuilder.addToFilterMap(username, filterMap, "username", "");
        GenericFilterMapBuilder.addToFilterMap(name, filterMap, "name", "");
        GenericFilterMapBuilder.addToFilterMap(email, filterMap, "email", "");

        var dtos = LogisticsOperatorAssembler.from(logisticsOperatorBean.getLogisticsOperators(filterMap, page, pageSize));
        long totalItems = logisticsOperatorBean.getLogisticsOperatorsCount(filterMap);
        long totalPages = (totalItems + pageSize - 1) / pageSize;
        PaginationMetadata paginationMetadata = new PaginationMetadata(page, pageSize, totalItems, totalPages, dtos.size());
        PaginationResponse<LogisticsOperatorDTO> paginationResponse = new PaginationResponse<>(dtos, paginationMetadata);
        return Response.ok(paginationResponse).build();
    }

    @GET
    @Path("{username}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    @Authenticated
    @RolesAllowed({"LogisticsOperator"})
    public Response get(@PathParam("username") String username) throws MyEntityNotFoundException {
        var principal = securityContext.getUserPrincipal();

        if (!principal.getName().equals(username)) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        LogisticsOperator logisticsOperator = logisticsOperatorBean.find(username);

        if (logisticsOperator != null) {
            return Response.ok(LogisticsOperatorAssembler.from(logisticsOperator)).build();
        }
        return Response.status(Response.Status.NOT_FOUND)
                .entity("ERROR_FINDING_LOGISTICS_OPERATOR")
                .build();
    }

    @POST
    @Path("/")
    @RolesAllowed({"LogisticsOperator"})
    public Response create(LogisticsOperatorDTO logisticsOperatorDTO)
            throws MyEntityExistsException, MyEntityNotFoundException, MyConstraintViolationException {
        logisticsOperatorBean.create(
                logisticsOperatorDTO.getUsername(),
                logisticsOperatorDTO.getPassword(),
                logisticsOperatorDTO.getName(),
                logisticsOperatorDTO.getEmail()
        );
        var logisticsOperator = logisticsOperatorBean.find(logisticsOperatorDTO.getUsername());
        return Response.status(Response.Status.CREATED).entity(LogisticsOperatorAssembler.from(logisticsOperator)).build();
    }

    @PUT
    @Path("{username}")
    @Authenticated
    @RolesAllowed({"LogisticsOperator"})
    public Response update(@PathParam("username") String username, ManufacturerDTO manufacturerDTO) throws MyEntityNotFoundException {
        var principal = securityContext.getUserPrincipal();
        if (!principal.getName().equals(username)) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        logisticsOperatorBean.update(
                username,
                manufacturerDTO.getName(),
                manufacturerDTO.getEmail()
        );
        var logisticsOperator = logisticsOperatorBean.find(username);
        return Response.ok(LogisticsOperatorAssembler.from(logisticsOperator)).build();
    }

    @DELETE
    @Path("{username}")
    @Authenticated
    @RolesAllowed({"LogisticsOperator"})
    public Response delete(@PathParam("username") String username) throws MyEntityNotFoundException {
        var principal = securityContext.getUserPrincipal();
        if (!principal.getName().equals(username)) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        LogisticsOperator logisticsOperator = logisticsOperatorBean.delete(username);
        return Response.status(Response.Status.OK).entity(LogisticsOperatorAssembler.from(logisticsOperator)).build();
    }

    @POST
    @Path("/{username}/email/send")
    @Authenticated
    public Response sendEmail(@PathParam("username") String username, EmailDTO email)
            throws MyEntityNotFoundException, MessagingException {
        LogisticsOperator logisticsOperator = logisticsOperatorBean.find(username);
        if (logisticsOperator == null) {
            throw new MyEntityNotFoundException("LogisticsOperator with username '" + username
                    + "' not found in our records.");
        }
        emailBean.send(logisticsOperator.getEmail(), email.getSubject(), email.getMessage());
        return Response.status(Response.Status.OK).entity("E-mail sent").build();
    }
}
