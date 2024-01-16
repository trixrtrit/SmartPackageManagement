package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.ws;


import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.assemblers.SensorAssembler;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.assemblers.SensorPackageAssembler;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.dtos.SensorDTO;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.ejbs.SensorBean;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.Sensor;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.pagination.PaginationMetadata;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.pagination.PaginationResponse;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.security.Authenticated;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.specifications.GenericFilterMapBuilder;

import java.util.HashMap;
import java.util.Map;

@Path("sensors")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class SensorService {
    @EJB
    private SensorBean sensorBean;

    @Context
    private SecurityContext securityContext;

    @GET
    @Path("/all")
    public Response getAll(@QueryParam("name") String name,
                           @DefaultValue("1") @QueryParam("page") int page,
                           @DefaultValue("10") @QueryParam("pageSize") int pageSize
    ) throws IllegalArgumentException {

        Map<String, String> filterMap = new HashMap<>();
        GenericFilterMapBuilder.addToFilterMap(name, filterMap, "name", "eq");

        var dtos = SensorAssembler.from(sensorBean.getSensors(filterMap, page, pageSize));
        long totalItems = sensorBean.getSensorsCount(filterMap);
        long totalPages = (totalItems + pageSize - 1) / pageSize;
        PaginationMetadata paginationMetadata = new PaginationMetadata(page, pageSize, totalItems, totalPages, dtos.size());
        PaginationResponse<SensorDTO> paginationResponse = new PaginationResponse<>(dtos, paginationMetadata);
        return Response.ok(paginationResponse).build();
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    public Response get(@PathParam("id") long id) throws MyEntityNotFoundException {
        Sensor sensor = sensorBean.find(id);

        if (sensor != null) {
            return Response.ok(SensorAssembler.from(sensor)).build();
        }
        return Response.status(Response.Status.NOT_FOUND)
                .entity("ERROR_FINDING_SENSOR")
                .build();
    }

    @GET
    @Path("{id}/measurements")
    @Authenticated
    @RolesAllowed({"LogisticsOperator"})
    public Response getSensorMeasurements(@PathParam("id") long id) throws MyEntityNotFoundException {
        Sensor sensor = sensorBean.getSensorMeasurements(id);
        if (sensor != null) {
            var dtos = SensorPackageAssembler.fromWithMeasurements(sensor.getSensorPackageList());
            return Response.ok(dtos).build();
        }
        return Response.status(Response.Status.NOT_FOUND)
                .entity("ERROR_FINDING_SENSOR")
                .build();
    }

    @GET
    @Path("{id}/packages")
    @Authenticated
    @RolesAllowed({"LogisticsOperator"})
    public Response getSensorPackages(@PathParam("id") long id) throws MyEntityNotFoundException {
        Sensor sensor = sensorBean.getSensorPackages(id);
        if (sensor != null) {
            var dtos = SensorPackageAssembler.fromWithPackages(sensor.getSensorPackageList());
            return Response.ok(dtos).build();
        }
        return Response.status(Response.Status.NOT_FOUND)
                .entity("ERROR_FINDING_SENSOR")
                .build();
    }

    @POST
    @Path("/")
    @Authenticated
    @RolesAllowed({"LogisticsOperator"})
    public Response create(SensorDTO sensorDTO)
            throws MyEntityExistsException, MyEntityNotFoundException, MyConstraintViolationException {
        long sensorId = sensorBean.create(
                sensorDTO.getName(),
                sensorDTO.getSensorType().getId()
                );
        var sensor = sensorBean.find(sensorId);
        return Response.status(Response.Status.CREATED).entity(SensorAssembler.from(sensor)).build();
    }

    @PUT
    @Path("{id}")
    @Authenticated
    @RolesAllowed({"LogisticsOperator"})
    public Response update(@PathParam("id") long id, SensorDTO sensorDTO)
            throws MyEntityNotFoundException, MyConstraintViolationException {
        sensorBean.update(
                id,
                sensorDTO.getName(),
                sensorDTO.getSensorType().getId()
        );
        var sensor = sensorBean.find(id);
        return Response.ok(SensorAssembler.from(sensor)).build();
    }

    @DELETE
    @Path("{id}")
    @Authenticated
    @RolesAllowed({"LogisticsOperator"})
    public Response delete(@PathParam("id") long id) throws MyEntityNotFoundException{
        Sensor sensor = sensorBean.delete(id);
        return Response.status(Response.Status.OK).entity(SensorAssembler.from(sensor)).build();
    }
}
