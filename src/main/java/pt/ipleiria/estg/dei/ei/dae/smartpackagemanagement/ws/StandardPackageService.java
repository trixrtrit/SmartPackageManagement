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
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.assemblers.StandardPackageProductAssembler;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.dtos.ProductDTO;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.dtos.SensorDTO;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.dtos.StandardPackageDTO;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.ejbs.PackageBean;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.ejbs.ProductBean;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.ejbs.StandardPackageBean;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.Package;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.StandardPackage;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.enums.PackageType;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.*;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.pagination.PaginationMetadata;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.pagination.PaginationResponse;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.security.Authenticated;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.specifications.GenericFilterMapBuilder;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.utils.EnumUtil;

import java.util.HashMap;
import java.util.Map;

@Path("standard-packages")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class StandardPackageService {
    @EJB
    private PackageBean packageBean;
    @EJB
    private StandardPackageBean standardPackageBean;

    @EJB
    ProductBean productBean;

    @Context
    private SecurityContext securityContext;

    @GET
    @Path("/all")
    @Authenticated
    @RolesAllowed({"LogisticsOperator"})
    public Response getAll(@QueryParam("code") long code,
                           @QueryParam("material") String material,
                           @QueryParam("packageType") String packageTypeString,
                           @DefaultValue("1") @QueryParam("page") int page,
                           @DefaultValue("10") @QueryParam("pageSize") int pageSize
    ) throws IllegalArgumentException {

        PackageType packageType;
        try {
           packageType  = EnumUtil.getEnumFromString(PackageType.class, packageTypeString);
        } catch (IllegalArgumentException e) {
            packageType = null;
        }

        Map<String, String> filterMap = new HashMap<>();
        GenericFilterMapBuilder.addToFilterMap(code, filterMap, "code", "eq");
        GenericFilterMapBuilder.addToFilterMap(material, filterMap, "material", "");
        GenericFilterMapBuilder.addToFilterMap(packageType, filterMap, "packageType", "");

        var dtos = StandardPackageAssembler.from(standardPackageBean.getStandardPackages(filterMap, page, pageSize));
        long totalItems = standardPackageBean.getStandardPackagesCount(filterMap);
        long totalPages = (totalItems + pageSize - 1) / pageSize;
        PaginationMetadata paginationMetadata = new PaginationMetadata(page, pageSize, totalItems, totalPages, dtos.size());
        PaginationResponse<StandardPackageDTO> paginationResponse = new PaginationResponse<>(dtos, paginationMetadata);
        return Response.ok(paginationResponse).build();
    }


    @GET
    @Path("/getForDelivery")
    @Authenticated
    @RolesAllowed({"LogisticsOperator"})
    public Response getAll(@QueryParam("productId") Long productId,
                           @QueryParam("packageType") String packageTypeString,
                           @DefaultValue("1") @QueryParam("page") int page,
                           @DefaultValue("10") @QueryParam("pageSize") int pageSize
    ) throws IllegalArgumentException {

        PackageType packageType;
        try {
            packageType  = EnumUtil.getEnumFromString(PackageType.class, packageTypeString);
        } catch (IllegalArgumentException e) {
            packageType = null;
        }

        if (productId == null || packageType == null){
            throw new IllegalArgumentException("The productId and the packageType are mandatory");
        }

        if(!productBean.exists(productId)){
            throw new IllegalArgumentException("Product not found");
        }

        var dtos = StandardPackageAssembler.from(standardPackageBean.getForDelivery(productId, packageType, page, pageSize));
        long totalItems = standardPackageBean.getForDeliveryCount(productId, packageType);
        long totalPages = (totalItems + pageSize - 1) / pageSize;
        PaginationMetadata paginationMetadata = new PaginationMetadata(page, pageSize, totalItems, totalPages, dtos.size());
        PaginationResponse<StandardPackageDTO> paginationResponse = new PaginationResponse<>(dtos, paginationMetadata);
        return Response.ok(paginationResponse).build();
    }

    @GET
    @Path("{code}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    @Authenticated
    @RolesAllowed({"LogisticsOperator"})
    public Response get(@PathParam("code") long code) throws MyEntityNotFoundException {
        StandardPackage standardPackage = standardPackageBean.find(code);

        if (standardPackage != null) {
            return Response.ok(StandardPackageAssembler.from(standardPackage)).build();
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
        StandardPackage standardPackage = standardPackageBean.getStandardPackageProducts(code);
        if (standardPackage != null) {
            var dtos = StandardPackageProductAssembler.from(standardPackage.getStandardPackageProducts());
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
        StandardPackage standardPackage = standardPackageBean.getPackageSensors(code);
        if (standardPackage != null) {
            var dtos = SensorPackageAssembler.from(standardPackage.getSensorPackageList());
            return Response.ok(dtos).build();
        }
        return Response.status(Response.Status.NOT_FOUND)
                .entity("ERROR_FINDING_PACKAGE")
                .build();
    }

    @GET
    @Path("{code}/measurements")
    @Authenticated
    @RolesAllowed({"LogisticsOperator", "Manufacturer", "Customer"})
    public Response getPackageMeasurements(@PathParam("code") long code)
            throws MyEntityNotFoundException, MyPackageMeasurementInvalidAccessException {

        Package aPackage = null;
        String username = securityContext.getUserPrincipal().getName();
        if(securityContext.isUserInRole("LogisticsOperator")) {
            aPackage = packageBean.getPackageMeasurements(code, StandardPackage.class);
        } else if (securityContext.isUserInRole("Manufacturer")){
            aPackage = packageBean.getPackageMeasurementsForUser(code, StandardPackage.class, username);
        }

        if (aPackage != null) {
            var dtos = SensorPackageAssembler.fromWithMeasurements(aPackage.getSensorPackageList());
            return Response.ok(dtos).build();
        }
        return Response.status(Response.Status.NOT_FOUND)
                .entity("ERROR_FINDING_PACKAGE")
                .build();
    }

    @POST
    @Path("/empty")
    @Authenticated
    @RolesAllowed({"LogisticsOperator", "Manufacturer"})
    public Response createEmpty(StandardPackageDTO standardPackageDTO)
            throws MyEntityExistsException, MyEntityNotFoundException, MyConstraintViolationException {
        if (isUnauthorizedAccess(standardPackageDTO.getPackageType()))
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("UNAUTHORIZED")
                    .build();
            long packageId = standardPackageBean.createEmpty(
                    standardPackageDTO.getCode(),
                    standardPackageDTO.getMaterial(),
                    standardPackageDTO.getPackageType(),
                    standardPackageDTO.getManufactureDate()
            );
            var standardPackage = standardPackageBean.find(packageId);
            return Response.status(Response.Status.CREATED).entity(StandardPackageAssembler.from(standardPackage)).build();
    }

    @POST
    @Path("/")
    @Authenticated
    @RolesAllowed({"LogisticsOperator", "Manufacturer"})
    public Response create(StandardPackageDTO standardPackageDTO)
            throws MyEntityExistsException, MyEntityNotFoundException, MyConstraintViolationException {
        if (isUnauthorizedAccess(standardPackageDTO.getPackageType()))
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("UNAUTHORIZED")
                    .build();
        var amount = standardPackageDTO.getInitialAmountCreation();
        if(amount <= 0){
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("AMOUNT_MUST_BE_GREATER_THAN_ZERO")
                    .build();
        }
        else if(amount == null || amount == 1){
            long packageId = standardPackageBean.create(
                    standardPackageDTO.getCode(),
                    standardPackageDTO.getMaterial(),
                    standardPackageDTO.getPackageType(),
                    standardPackageDTO.getManufactureDate(),
                    standardPackageDTO.getInitialProductId()
            );
            var standardPackage = standardPackageBean.find(packageId);
            return Response.status(Response.Status.CREATED).entity(StandardPackageAssembler.from(standardPackage)).build();
        }
        else {
            long result = standardPackageBean.createMany(
                    standardPackageDTO.getCode(),
                    standardPackageDTO.getMaterial(),
                    standardPackageDTO.getPackageType(),
                    standardPackageDTO.getManufactureDate(),
                    standardPackageDTO.getInitialProductId(),
                    standardPackageDTO.getInitialAmountCreation()
            );
            if (result >= 1) {
                if(result == amount){
                    return Response.status(Response.Status.CREATED).entity("Success: " + amount + " Packages were created").build();
                }
                else{
                    return Response.status(207).entity("Only " + result + " Packages were created").build();
                }
            }
        }
        return Response.status(Response.Status.BAD_REQUEST)
                .entity("ERROR_CREATING_PACKAGE")
                .build();
    }

    private boolean isUnauthorizedAccess(PackageType packageType) {
        boolean unauthorizedTertiary = packageType == PackageType.TERTIARY && !isRoleAuthorizedTertiary();
        boolean unauthorizedNonTertiary = (packageType == PackageType.PRIMARY ||
                packageType == PackageType.SECONDARY) && isRoleAuthorizedTertiary();

        return unauthorizedTertiary || unauthorizedNonTertiary;
    }

    @PUT
    @Path("{code}")
    @Authenticated
    @RolesAllowed({"LogisticsOperator"})
    public Response update(@PathParam("code") long code, StandardPackageDTO standardPackageDTO)
            throws MyEntityNotFoundException, MyConstraintViolationException {

        var standardPackage = standardPackageBean.update(
                code,
                standardPackageDTO.getMaterial(),
                standardPackageDTO.getPackageType()
        );
        return Response.ok(StandardPackageAssembler.from(standardPackage)).build();
    }

    @PUT
    @Path("{code}/set-product")
    @Authenticated
    @RolesAllowed({"LogisticsOperator"})
    public Response addProduct(@PathParam("code") long code, ProductDTO product)
            throws MyEntityNotFoundException, MyPackageProductAssociationViolationException {

        standardPackageBean.addProductToPackage(
                code,
                product.getId()
        );
        var standardPackage = standardPackageBean.find(code);
        return Response.ok(StandardPackageAssembler.from(standardPackage)).build();
    }

    @PUT
    @Path("{code}/unset-product")
    @Authenticated
    @RolesAllowed({"LogisticsOperator"})
    public Response removeProduct(@PathParam("code") long code, ProductDTO product)
            throws MyEntityNotFoundException, MyPackageProductAssociationViolationException {

        standardPackageBean.addProductToPackage(
                code,
                product.getId()
        );
        var standardPackage = standardPackageBean.find(code);
        return Response.ok(StandardPackageAssembler.from(standardPackage)).build();
    }

    @PUT
    @Path("{code}/set-sensor")
    @Authenticated
    @RolesAllowed({"LogisticsOperator"})
    public Response addSensor(@PathParam("code") long code, SensorDTO sensor)
            throws MyEntityNotFoundException, MyEntityExistsException {

        standardPackageBean.addSensorToPackage(
                code,
                sensor.getId()
        );
        var standardPackage = standardPackageBean.find(code);
        return Response.ok(StandardPackageAssembler.fromWithSensors(standardPackage)).build();
    }

    @PUT
    @Path("{code}/unset-sensor")
    @Authenticated
    @RolesAllowed({"LogisticsOperator"})
    public Response removeSensor(@PathParam("code") long code, SensorDTO sensor)
            throws MyEntityNotFoundException {

        standardPackageBean.removeSensorFromPackage(
                code,
                sensor.getId()
        );
        var standardPackage = standardPackageBean.find(code);
        return Response.ok(StandardPackageAssembler.fromWithSensors(standardPackage)).build();
    }

    @DELETE
    @Path("{code}")
    @Authenticated
    @RolesAllowed({"LogisticsOperator"})
    public Response delete(@PathParam("code") long code) throws MyEntityNotFoundException {
        Package aPackage = packageBean.delete(code, StandardPackage.class);
        return Response.status(Response.Status.OK).entity(StandardPackageAssembler.from((StandardPackage)aPackage)).build();
    }

    @PUT
    @Path("{code}/active-status")
    @Authenticated
    @RolesAllowed({"Manufacturer", "LogisticsOperator"})
    public Response changeActiveStatus(@PathParam("code") long code)
            throws MyEntityNotFoundException {
        var standardPackage = standardPackageBean.find(code);
        if (standardPackage == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("ERROR_FINDING_PACKAGE")
                    .build();
        }

        if (isUnauthorizedAccess(standardPackage.getPackageType())) return Response.status(Response.Status.UNAUTHORIZED)
                .entity("UNAUTHORIZED")
                .build();

        packageBean.changeActiveStatus(code, StandardPackage.class);
        return Response.ok(StandardPackageAssembler.from(standardPackage)).build();
    }

    private boolean isRoleAuthorizedTertiary() {
        return securityContext.isUserInRole("LogisticsOperator");
    }
}
