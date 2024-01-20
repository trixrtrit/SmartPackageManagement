package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.ws;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.assemblers.SensorPackageAssembler;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.assemblers.StandardPackageAssembler;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.assemblers.TransportPackageAssembler;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.assemblers.TransportPackageStandardPackagesAssembler;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.dtos.SensorDTO;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.dtos.StandardPackageDTO;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.dtos.TransportPackageDTO;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.ejbs.PackageBean;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.ejbs.TransportPackageBean;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.Package;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.StandardPackage;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.TransportPackage;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyPackageMeasurementInvalidAccessException;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.pagination.PaginationMetadata;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.pagination.PaginationResponse;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.security.Authenticated;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.specifications.GenericFilterMapBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("transport-packages")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class TransportPackageService {
    @EJB
    private PackageBean packageBean;
    @EJB
    private TransportPackageBean transportPackageBean;

    @Context
    private SecurityContext securityContext;

    @GET
    @Path("/all")
    @Authenticated
    @RolesAllowed({"LogisticsOperator", "Customer"})
    public Response getAll(@QueryParam("code") long code,
                           @QueryParam("material") String material,
                           @DefaultValue("1") @QueryParam("page") int page,
                           @DefaultValue("10") @QueryParam("pageSize") int pageSize
    ) throws IllegalArgumentException {

        Map<String, String> filterMap = new HashMap<>();
        GenericFilterMapBuilder.addToFilterMap(code, filterMap, "code", "eq");
        GenericFilterMapBuilder.addToFilterMap(material, filterMap, "material", "");

        List<TransportPackage> transportPackages = new ArrayList<>();
        String username = securityContext.getUserPrincipal().getName();
        if(securityContext.isUserInRole("LogisticsOperator")) {
            transportPackages = transportPackageBean.getTransportPackages(filterMap, page, pageSize);
        } else if (securityContext.isUserInRole("Customer")) {
            transportPackages = transportPackageBean.filterTransportPackagesByUserOwnership(
                    transportPackageBean.getTransportPackages(filterMap, page, pageSize), username
            );
        }
        var dtos = TransportPackageAssembler.from(transportPackages);
        long totalItems = transportPackageBean.getTransportPackagesCount(filterMap);
        long totalPages = (totalItems + pageSize - 1) / pageSize;
        PaginationMetadata paginationMetadata = new PaginationMetadata(page, pageSize, totalItems, totalPages, dtos.size());
        PaginationResponse<TransportPackageDTO> paginationResponse = new PaginationResponse<>(dtos, paginationMetadata);
        return Response.ok(paginationResponse).build();
    }

    @GET
    @Path("{code}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    @Authenticated
    @RolesAllowed({"LogisticsOperator"})
    public Response get(@PathParam("code") long code) throws MyEntityNotFoundException {
        TransportPackage transportPackage = transportPackageBean.find(code);

        if (transportPackage != null) {
            return Response.ok(TransportPackageAssembler.from(transportPackage)).build();
        }
        return Response.status(Response.Status.NOT_FOUND)
                .entity("ERROR_FINDING_PACKAGE")
                .build();
    }

    @GET
    @Path("{code}/sensors")
    @Authenticated
    @RolesAllowed({"LogisticsOperator"})
    public Response getPackageSensors(@PathParam("code") long code) throws MyEntityNotFoundException {
        TransportPackage transportPackage = transportPackageBean.getPackageSensors(code);
        if (transportPackage != null) {
            var dtos = SensorPackageAssembler.from(transportPackage.getSensorPackageList());
            return Response.ok(dtos).build();
        }
        return Response.status(Response.Status.NOT_FOUND)
                .entity("ERROR_FINDING_PACKAGE")
                .build();
    }

    @GET
    @Path("{code}/measurements")
    @Authenticated
    @RolesAllowed({"LogisticsOperator", "Customer"})
    public Response getPackageMeasurements(@PathParam("code") long code)
            throws MyEntityNotFoundException, MyPackageMeasurementInvalidAccessException {

        Package aPackage = null;
        String username = securityContext.getUserPrincipal().getName();
        if(securityContext.isUserInRole("LogisticsOperator")) {
            aPackage = packageBean.getPackageMeasurements(code, TransportPackage.class);
        } else if (securityContext.isUserInRole("Customer")){
            aPackage = packageBean.getPackageMeasurementsForUser(code, TransportPackage.class, username);
        }
        if (aPackage != null) {
            var dtos = SensorPackageAssembler.fromWithMeasurements(aPackage.getSensorPackageList());
            return Response.ok(dtos).build();
        }
        return Response.status(Response.Status.NOT_FOUND)
                .entity("ERROR_FINDING_PACKAGE")
                .build();
    }

    @GET
    @Path("{code}/packages")
    @Authenticated
    @RolesAllowed({"LogisticsOperator"})
    public Response getTransportStandardPackages(@PathParam("code") long code) throws MyEntityNotFoundException {
        TransportPackage transportPackage = transportPackageBean.getTransportStandardPackages(code);
        if (transportPackage != null) {
            var dtos =
                    TransportPackageStandardPackagesAssembler.from(transportPackage.getTransportPackageStandardPackages());
            return Response.ok(dtos).build();
        }
        return Response.status(Response.Status.NOT_FOUND)
                .entity("ERROR_FINDING_PACKAGE")
                .build();
    }

    @POST
    @Path("/")
    @Authenticated
    @RolesAllowed({"LogisticsOperator"})
    public Response create(TransportPackageDTO transportPackageDTO)
            throws MyEntityExistsException, MyEntityNotFoundException, MyConstraintViolationException {
        long packageId = transportPackageBean.create(
                transportPackageDTO.getCode(),
                transportPackageDTO.getMaterial()
        );
        var transportPackage = transportPackageBean.find(packageId);
        return Response.status(Response.Status.CREATED).entity(TransportPackageAssembler.from(transportPackage)).build();
    }

    @PUT
    @Path("{code}")
    @Authenticated
    @RolesAllowed({"LogisticsOperator"})
    public Response update(@PathParam("code") long code, TransportPackageDTO transportPackageDTO)
            throws MyEntityNotFoundException, MyConstraintViolationException {

        var transportPackage = transportPackageBean.update(
                code,
                transportPackageDTO.getMaterial()
        );
        return Response.ok(TransportPackageAssembler.from(transportPackage)).build();
    }

    @PUT
    @Path("{code}/set-sensor")
    @Authenticated
    @RolesAllowed({"LogisticsOperator"})
    public Response addSensor(@PathParam("code") long code, SensorDTO sensor)
            throws MyEntityNotFoundException, MyEntityExistsException {

        transportPackageBean.addSensorToPackage(
                code,
                sensor.getId()
        );
        var transportPackage = transportPackageBean.find(code);
        return Response.ok(TransportPackageAssembler.fromWithSensors(transportPackage)).build();
    }

    @PUT
    @Path("{code}/unset-sensor")
    @Authenticated
    @RolesAllowed({"LogisticsOperator"})
    public Response removeSensor(@PathParam("code") long code, SensorDTO sensor)
            throws MyEntityNotFoundException {

        transportPackageBean.removeSensorFromPackage(
                code,
                sensor.getId()
        );
        var transportPackage = transportPackageBean.find(code);
        return Response.ok(TransportPackageAssembler.fromWithSensors(transportPackage)).build();
    }

    @PUT
    @Path("{code}/set-package")
    @Authenticated
    @RolesAllowed({"LogisticsOperator"})
    public Response addStandardPkgToTransportPkg(@PathParam("code") long code, StandardPackageDTO standardPackage)
            throws MyEntityNotFoundException, MyEntityExistsException {

        transportPackageBean.addStandardPkgToTransportPkg(
                code,
                standardPackage.getCode()
        );
        var transportPackage = transportPackageBean.find(code);
        return Response.ok(TransportPackageAssembler.fromWithPackages(transportPackage)).build();
    }

    @PUT
    @Path("{code}/unset-package")
    @Authenticated
    @RolesAllowed({"LogisticsOperator"})
    public Response removeStandardPkgFromTransportPkg(@PathParam("code") long code, StandardPackageDTO standardPackage)
            throws MyEntityNotFoundException {

        transportPackageBean.removeStandardPkgFromTransportPkg(
                code,
                standardPackage.getCode()
        );
        var transportPackage = transportPackageBean.find(code);
        return Response.ok(TransportPackageAssembler.fromWithPackages(transportPackage)).build();
    }

    @DELETE
    @Path("{code}")
    @Authenticated
    @RolesAllowed({"LogisticsOperator"})
    public Response delete(@PathParam("code") long code) throws MyEntityNotFoundException {
        Package aPackage = packageBean.delete(code, TransportPackage.class);
        return Response.status(Response.Status.OK).entity(TransportPackageAssembler.from((TransportPackage) aPackage)).build();
    }

    @PUT
    @Path("{code}/active-status")
    @Authenticated
    @RolesAllowed({"Manufacturer", "LogisticsOperator"})
    public Response changeActiveStatus(@PathParam("code") long code)
            throws MyEntityNotFoundException {
        var transportPackage = transportPackageBean.find(code);
        if (transportPackage == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("ERROR_FINDING_PACKAGE")
                    .build();
        }
        packageBean.changeActiveStatus(code, TransportPackage.class);
        return Response.ok(TransportPackageAssembler.from(transportPackage)).build();
    }
}
