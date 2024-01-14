package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.ws;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.SecurityContext;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.dtos.MeasurementDTO;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.ejbs.MeasurementBean;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.Measurement;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.pagination.PaginationMetadata;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.pagination.PaginationResponse;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.security.Authenticated;

import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

@Path("measurements")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class MeasurementService {
    @EJB
    private MeasurementBean measurementBean;

    @Context
    private SecurityContext securityContext;

    private MeasurementDTO toDTO (Measurement measurement){
        return new MeasurementDTO(
                measurement.getMeasurement(),
                measurement.getTimestamp(),
                measurement.getSensorPackage().getSensor().getId(),
                measurement.getSensorPackage().getaPackage().getCode()
        );
    }


        private List<MeasurementDTO> toDTOs(List<Measurement> measurements) {
            return measurements.stream().map(this::toDTO).collect(Collectors.toList());
        }

    @GET
    @Path("/all")
    @Authenticated
    @RolesAllowed({"LogisticsOperator"})
    public Response getAll(@QueryParam("sensorId") Long sensorId,
                           @QueryParam("packageCode") String packageCode,
                           @QueryParam("startDate") String startDateStr,
                           @QueryParam("endDate") String endDateStr,
                           @QueryParam("isActive") boolean isActive,
                           @DefaultValue("1") @QueryParam("page") int page,
                           @DefaultValue("10") @QueryParam("pageSize") int pageSize
    ) {
        Instant startDate = null;
        Instant endDate = null;
        try {
            if(startDateStr != null) {
                startDate = Instant.parse(startDateStr);
            }
            if(endDateStr != null) {
                endDate = Instant.parse(endDateStr);
            }
        } catch ( DateTimeParseException e ) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid date format").build();
        }
        var dtos = toDTOs(measurementBean.getMeasurements(
                sensorId,
                packageCode,
                startDate,
                endDate,
                isActive,
                page,
                pageSize)
        );
        long totalItems = measurementBean.getMeasurementsCount(sensorId, packageCode, startDate, endDate, isActive);
        long totalPages = (totalItems + pageSize - 1) / pageSize;
        PaginationMetadata paginationMetadata = new PaginationMetadata(page, pageSize, totalItems, totalPages, dtos.size());
        PaginationResponse<MeasurementDTO> paginationResponse = new PaginationResponse<>(dtos, paginationMetadata);
        return Response.ok(paginationResponse).build();
    }

    @POST
    @Path("/")
    public Response create(MeasurementDTO measurementDTO)
            throws MyEntityExistsException, MyEntityNotFoundException, MyConstraintViolationException {
        long measurementId = measurementBean.create(
                measurementDTO.getMeasurement(),
                measurementDTO.getPackageCode(),
                measurementDTO.getSensorId()
        );
        var measurement = measurementBean.find(measurementId);
        return Response.status(Response.Status.CREATED).entity(toDTO(measurement)).build();
    }
}