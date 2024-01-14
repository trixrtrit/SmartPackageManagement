package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.ws;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.assemblers.LogisticsOperatorAssembler;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.dtos.LogisticsOperatorDTO;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.dtos.ManufacturerDTO;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.ejbs.LogisticsOperatorBean;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.LogisticsOperator;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.security.Authenticated;

import java.util.List;

@Path("logistics-operator")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class LogisticsOperatorService {

    @EJB
    private LogisticsOperatorBean logisticsOperatorBean;

    @Context
    private SecurityContext securityContext;

    @GET
    @Path("/all")
    public List<LogisticsOperatorDTO> getAll() {
        return LogisticsOperatorAssembler.from(logisticsOperatorBean.getLogisticsOperators());
    }

    @GET
    @Path("{username}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    @Authenticated
    @RolesAllowed({"LogisticsOperator"})
    public Response get(@PathParam("username") String username) throws MyEntityNotFoundException {
        var principal = securityContext.getUserPrincipal();

        if (!principal.getName().equals(username)){
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
        if (!principal.getName().equals(username)){
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
    public Response delete(@PathParam("username") String username) throws MyEntityNotFoundException{
        var principal = securityContext.getUserPrincipal();
        if (!principal.getName().equals(username)){
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        LogisticsOperator logisticsOperator = logisticsOperatorBean.delete(username);
        return Response.status(Response.Status.OK).entity(LogisticsOperatorAssembler.from(logisticsOperator)).build();
    }
}
