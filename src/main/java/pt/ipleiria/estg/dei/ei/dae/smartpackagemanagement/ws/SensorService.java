package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.ws;


import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.dtos.SensorDTO;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.ejbs.SensorBean;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.Sensor;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.security.Authenticated;

import java.util.List;
import java.util.stream.Collectors;

@Path("sensors")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class SensorService {
    @EJB
    private SensorBean sensorBean;

    @Context
    private SecurityContext securityContext;

    private SensorDTO toDTO(Sensor sensor) {
        return new SensorDTO(
                sensor.getId(),
                sensor.getName(),
                sensor.getSensorType(),
                sensor.getaPackage().getId()
        );
    }

    private List<SensorDTO> toDTOs(List<Sensor> sensors) {
        return sensors.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @GET
    @Path("/all")
    public List<SensorDTO> getAll() {
        return toDTOs(sensorBean.getSensors());
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    public Response get(@PathParam("id") long id) throws MyEntityNotFoundException {
        Sensor sensor = sensorBean.find(id);

        if (sensor != null) {
            return Response.ok(toDTO(sensor)).build();
        }
        return Response.status(Response.Status.NOT_FOUND)
                .entity("ERROR_FINDING_SENSOR")
                .build();
    }

    @POST
    @Path("/")
    @Authenticated
    @RolesAllowed({"Manufacturer"}) //todo rever
    public Response create(SensorDTO sensorDTO)
            throws MyEntityExistsException, MyEntityNotFoundException, MyConstraintViolationException {
        sensorBean.create(
                sensorDTO.getName(),
                sensorDTO.getPackageId(),
                sensorDTO.getSensorType()
        );
        var sensor = sensorBean.find(sensorDTO.getPackageId());
        return Response.status(Response.Status.CREATED).entity(toDTO(sensor)).build();
    }

    @DELETE
    @Path("{id}")
    @Authenticated
    @RolesAllowed({"Manufacturer"})//todo rever
    public Response delete(@PathParam("id") long id) throws MyEntityNotFoundException{
        Sensor sensor = sensorBean.delete(id);
        return Response.status(Response.Status.OK).entity(toDTO(sensor)).build();
    }



}
