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
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.pagination.PaginationMetadata;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.pagination.PaginationResponse;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.security.Authenticated;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.specifications.GenericFilterMapBuilder;

import java.util.HashMap;
import java.util.Map;

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
    public Response getAll(@QueryParam("name") String name,
                           @QueryParam("unit") String measurementUnit,
                           @DefaultValue("1") @QueryParam("page") int page,
                           @DefaultValue("10") @QueryParam("pageSize") int pageSize
    ) throws IllegalArgumentException {

        Map<String, String> filterMap = new HashMap<>();
        GenericFilterMapBuilder.addToFilterMap(name, filterMap, "name", "");
        GenericFilterMapBuilder.addToFilterMap(measurementUnit, filterMap, "measurementUnit", "");

        var dtos = SensorTypeAssembler.from(sensorTypeBean.getProductParameters(filterMap, page, pageSize));
        long totalItems = sensorTypeBean.getSensorTypeCount(filterMap);
        long totalPages = (totalItems + pageSize - 1) / pageSize;
        PaginationMetadata paginationMetadata = new PaginationMetadata(page, pageSize, totalItems, totalPages, dtos.size());
        PaginationResponse<SensorTypeDTO> paginationResponse = new PaginationResponse<>(dtos, paginationMetadata);
        return Response.ok(paginationResponse).build();
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
