package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.ws;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.assemblers.PackageAssembler;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.assemblers.ProductAssembler;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.assemblers.SensorPackageAssembler;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.dtos.*;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.ejbs.PackageBean;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.Package;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.enums.PackageType;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyPackageProductAssociationViolationException;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.security.Authenticated;

import java.util.List;

@Path("packages")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class PackageService {
    @EJB
    private PackageBean packageBean;

    @Context
    private SecurityContext securityContext;

    //TODO: adicionar DTO de orderItems
    @GET
    @Path("/all")
    @RolesAllowed({"LogisticsOperator"})
    public List<PackageDTO> getAll() {
        return PackageAssembler.from(packageBean.getPackages());
    }

    @GET
    @Path("{code}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    @Authenticated
    @RolesAllowed({"LogisticsOperator"})
    public Response get(@PathParam("code") long code) throws MyEntityNotFoundException {
        Package aPackage = packageBean.find(code);

        if (aPackage != null) {
            return Response.ok(PackageAssembler.from(aPackage)).build();
        }
        return Response.status(Response.Status.NOT_FOUND)
                .entity("ERROR_FINDING_PACKAGE")
                .build();
    }

    @GET
    @Path("{code}/products")
    @Authenticated
    @RolesAllowed({"LogisticsOperator"})
    public Response getPackageProducts(@PathParam("code") long code) throws MyEntityNotFoundException {
        Package aPackage = packageBean.getPackageProducts(code);
        if (aPackage != null) {
            var dtos = ProductAssembler.from(aPackage.getProducts());
            return Response.ok(dtos).build();
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
        Package aPackage = packageBean.getPackageSensors(code);
        if (aPackage != null) {
            var dtos = SensorPackageAssembler.from(aPackage.getSensorPackageList());
            return Response.ok(dtos).build();
        }
        return Response.status(Response.Status.NOT_FOUND)
                .entity("ERROR_FINDING_PACKAGE")
                .build();
    }

    //TODO: validar de quem e o pkg para verem as mediçoes
    @GET
    @Path("{code}/measurements")
    @Authenticated
    @RolesAllowed({"LogisticsOperator", "Manufacturer", "Customer"})
    public Response getPackageMeasurements(@PathParam("code") long code) throws MyEntityNotFoundException {
        Package aPackage = packageBean.getPackageMeasurements(code);
        if (aPackage != null) {
            var dtos = SensorPackageAssembler.fromWithMeasurements(aPackage.getSensorPackageList());
            return Response.ok(dtos).build();
        }
        return Response.status(Response.Status.NOT_FOUND)
                .entity("ERROR_FINDING_PACKAGE")
                .build();
    }

    @POST
    @Path("/")
    @RolesAllowed({"LogisticsOperator"})
    public Response create(PackageDTO packageDTO)
            throws MyEntityExistsException, MyEntityNotFoundException, MyConstraintViolationException {
        long packageId = packageBean.create(
                packageDTO.getCode(),
                packageDTO.getMaterial(),
                packageDTO.getPackageType()
        );
        var aPackage = packageBean.find(packageId);
        return Response.status(Response.Status.CREATED).entity(PackageAssembler.from(aPackage)).build();
    }

    @PUT
    @Path("{code}")
    @Authenticated
    @RolesAllowed({"LogisticsOperator"})
    public Response update(@PathParam("code") long code, PackageDTO packageDTO)
            throws MyEntityNotFoundException, MyConstraintViolationException {

        packageBean.update(
                code,
                packageDTO.getMaterial(),
                packageDTO.getPackageType()
        );
        var aPackage = packageBean.find(code);
        return Response.ok(PackageAssembler.from(aPackage)).build();
    }

    @PUT
    @Path("{code}/set-product")
    @Authenticated
    @RolesAllowed({"LogisticsOperator"})
    public Response addProduct(@PathParam("code") long code, ProductDTO product)
            throws MyEntityNotFoundException, MyPackageProductAssociationViolationException {

        packageBean.addProductToPackage(
                code,
                product.getId()
        );
        var aPackage = packageBean.find(code);
        return Response.ok(PackageAssembler.fromWithProducts(aPackage)).build();
    }

    @PUT
    @Path("{code}/unset-product")
    @Authenticated
    @RolesAllowed({"LogisticsOperator"})
    public Response removeProduct(@PathParam("code") long code, ProductDTO product)
            throws MyEntityNotFoundException, MyConstraintViolationException {

        packageBean.removeProductFromPackage(
                code,
                product.getId()
        );
        var aPackage = packageBean.find(code);
        return Response.ok(PackageAssembler.fromWithProducts(aPackage)).build();
    }

    @PUT
    @Path("{code}/set-sensor")
    @Authenticated
    @RolesAllowed({"LogisticsOperator"})
    public Response addSensor(@PathParam("code") long code, SensorDTO sensor)
            throws MyEntityNotFoundException, MyEntityExistsException {

        packageBean.addSensorToPackage(
                code,
                sensor.getId()
        );
        var aPackage = packageBean.find(code);
        return Response.ok(PackageAssembler.fromWithSensors(aPackage)).build();
    }

    @PUT
    @Path("{code}/unset-sensor")
    @Authenticated
    @RolesAllowed({"LogisticsOperator"})
    public Response removeSensor(@PathParam("code") long code, SensorDTO sensor)
            throws MyEntityNotFoundException {

        packageBean.removeSensorFromPackage(
                code,
                sensor.getId()
        );
        var aPackage = packageBean.find(code);
        return Response.ok(PackageAssembler.fromWithSensors(aPackage)).build();
    }

    @DELETE
    @Path("{code}")
    @Authenticated
    @RolesAllowed({"LogisticsOperator"})
    public Response delete(@PathParam("code") long code) throws MyEntityNotFoundException {
        Package aPackage = packageBean.delete(code);
        return Response.status(Response.Status.OK).entity(PackageAssembler.from(aPackage)).build();
    }

    @PUT
    @Path("{code}/active-status")
    @Authenticated
    @RolesAllowed({"Manufacturer", "LogisticsOperator"})
    public Response changeActiveStatus(@PathParam("code") long code)
            throws MyEntityNotFoundException {
        var aPackage = packageBean.find(code);
        if (aPackage == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("ERROR_FINDING_PACKAGE")
                    .build();
        }

        boolean unauthorizedTertiary = aPackage.getPackageType() == PackageType.TERTIARY && !isRoleAuthorizedTertiary();
        boolean unauthorizedNonTertiary = (aPackage.getPackageType() == PackageType.PRIMARY ||
                aPackage.getPackageType() == PackageType.SECONDARY) && isRoleAuthorizedTertiary();

        if (unauthorizedTertiary || unauthorizedNonTertiary) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("UNAUTHORIZED")
                    .build();
        }

        packageBean.changeActiveStatus(code);
        return Response.ok(PackageAssembler.from(aPackage)).build();
    }

    private boolean isRoleAuthorizedTertiary() {
        return securityContext.isUserInRole("LogisticsOperator");
    }
}
