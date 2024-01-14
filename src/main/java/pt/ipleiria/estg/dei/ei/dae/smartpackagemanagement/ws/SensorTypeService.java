package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.ws;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.assemblers.SensorTypeAssembler;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.dtos.SensorTypeDTO;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.ejbs.SensorTypeBean;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.security.Authenticated;

import java.util.List;

@Path("sensor-types")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class SensorTypeService {
    @EJB
    private SensorTypeBean sensorTypeBean;

    @Context
    private SecurityContext securityContext;

    @GET
    @Path("/all")
    public List<SensorTypeDTO> getAll() {
        return SensorTypeAssembler.from(sensorTypeBean.getProductParameters());
    }

    @POST
    @Path("/")
    @Authenticated
    @RolesAllowed({"Manufacturer"})
    public Response create(SensorTypeDTO sensorTypeDTO)
            throws MyEntityExistsException, MyEntityNotFoundException, MyConstraintViolationException {
        var id = sensorTypeBean.create(
                sensorTypeDTO.getName(),
                sensorTypeDTO.getMeasurementUnit()
        );
        var sensorType = sensorTypeBean.find(id);
        return Response.status(Response.Status.CREATED).entity(SensorTypeAssembler.from(sensorType)).build();
    }

    @PUT
    @Path("{id}")
    @Authenticated
    @RolesAllowed({"Manufacturer"})
    public Response update(@PathParam("id") long id, SensorTypeDTO sensorTypeDTO)
            throws MyEntityNotFoundException, MyConstraintViolationException {
        sensorTypeBean.update(
                id,
                sensorTypeDTO.getName(),
                sensorTypeDTO.getMeasurementUnit()
        );
        var sensorType = sensorTypeBean.find(id);
        return Response.ok(SensorTypeAssembler.from(sensorType)).build();
    }

    @DELETE
    @Path("{id}")
    @Authenticated
    @RolesAllowed({"Manufacturer"})
    public Response delete(@PathParam("id") long id) throws MyEntityNotFoundException{
        var sensorType = sensorTypeBean.delete(id);
        return Response.status(Response.Status.OK).entity(SensorTypeAssembler.from(sensorType)).build();
    }
}
