package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.ws;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.SecurityContext;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.assemblers.MeasurementAssembler;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.dtos.MeasurementDTO;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.ejbs.MeasurementBean;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.pagination.PaginationMetadata;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.pagination.PaginationResponse;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.security.Authenticated;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.utils.DateUtil;

import java.util.Date;

@Path("measurements")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class MeasurementService {
    @EJB
    private MeasurementBean measurementBean;

    @Context
    private SecurityContext securityContext;

    @GET
    @Path("/all")
    @Authenticated
    @RolesAllowed({"LogisticsOperator"})
    public Response getAll(@QueryParam("sensorId") Long sensorId,
                           @QueryParam("packageCode") String packageCode,
                           @QueryParam("startTime") Long startTime,
                           @QueryParam("endTime") Long endTime,
                           @QueryParam("isActive") boolean isActive,
                           @DefaultValue("1") @QueryParam("page") int page,
                           @DefaultValue("10") @QueryParam("pageSize") int pageSize
    ) {
        Date[] dates = DateUtil.parseDates(startTime, endTime);
        Date startDate = dates[0];
        Date endDate = dates[1];
        var dtos = MeasurementAssembler.from(measurementBean.getMeasurements(
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
                measurementDTO.getSensorPackageDTO().getaPackage().getCode(),
                measurementDTO.getSensorPackageDTO().getSensor().getId()
        );
        var measurement = measurementBean.find(measurementId);
        return Response.status(Response.Status.CREATED).entity(MeasurementAssembler.from(measurement)).build();
    }
}
