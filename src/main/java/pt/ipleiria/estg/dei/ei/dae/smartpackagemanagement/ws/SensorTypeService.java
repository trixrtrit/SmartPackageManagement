package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.ws;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.dtos.SensorTypeDTO;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.ejbs.SensorTypeBean;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.SensorType;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.security.Authenticated;

import java.util.List;
import java.util.stream.Collectors;

@Path("sensor-types")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class SensorTypeService {
    @EJB
    private SensorTypeBean sensorTypeBean;

    @Context
    private SecurityContext securityContext;

    private SensorTypeDTO toDTO(SensorType sensorType) {
        return new SensorTypeDTO(
                sensorType.getId(),
                sensorType.getName(),
                sensorType.getMeasurementUnit()
        );
    }

    private List<SensorTypeDTO> toDTOs(List<SensorType> sensorTypes) {
        return sensorTypes.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @GET
    @Path("/all")
    public List<SensorTypeDTO> getAll() {
        return toDTOs(sensorTypeBean.getProductParameters());
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
        return Response.status(Response.Status.CREATED).entity(toDTO(sensorType)).build();
    }
}
