package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.ws;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.dtos.PackageDTO;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.dtos.ProductDTO;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.dtos.SensorDTO;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.ejbs.PackageBean;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.Package;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.Product;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.Sensor;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.security.Authenticated;

import java.util.List;
import java.util.stream.Collectors;

@Path("packages")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class PackageService {
    @EJB
    private PackageBean packageBean;

    //TODO: adicionar DTO de orderItems
    private PackageDTO toDTO(Package aPackage) {
        return new PackageDTO(
                aPackage.getId(),
                aPackage.getMaterial(),
                aPackage.getPackageType()
        );
    }

    private List<PackageDTO> toDTOs(List<Package> aPackages) {
        return aPackages.stream().map(this::toDTO).collect(Collectors.toList());
    }

    private PackageDTO toDTOProducts(Package aPackage) {
        return new PackageDTO(
                aPackage.getId(),
                aPackage.getMaterial(),
                aPackage.getPackageType(),
                productsToDTOs(aPackage.getProducts())
        );
    }

    private List<PackageDTO> toDTOsProducts(List<Package> aPackages) {
        return aPackages.stream().map(this::toDTOProducts).collect(Collectors.toList());
    }

    private ProductDTO productToDTO(Product product) {
        return new ProductDTO(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.isActive(),
                product.getManufacturer().getUsername(),
                product.getProductReference()
        );
    }

    private List<ProductDTO> productsToDTOs(List<Product> products) {
        return products.stream().map(this::productToDTO).collect(Collectors.toList());
    }


    private PackageDTO toDTOSensors(Package aPackage) {
        return new PackageDTO(
                aPackage.getId(),
                aPackage.getMaterial(),
                aPackage.getPackageType(),
                sensorsToDTOs(aPackage.getSensors()),
                true
        );
    }

    private List<PackageDTO> toDTOsSensors(List<Package> aPackages) {
        return aPackages.stream().map(this::toDTOSensors).collect(Collectors.toList());
    }

   private SensorDTO sensorToDTO(Sensor sensor) {
        return new SensorDTO();
    }

    private List<SensorDTO> sensorsToDTOs(List<Sensor> sensors) {
        return sensors.stream().map(this::sensorToDTO).collect(Collectors.toList());
    }

    @GET
    @Path("/all")
    @RolesAllowed({"LogisticsOperator"})
    public List<PackageDTO> getAll() {
        return toDTOs(packageBean.getPackages());
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    @Authenticated
    @RolesAllowed({"LogisticsOperator"})
    public Response get(@PathParam("id") long id) throws MyEntityNotFoundException {
        Package aPackage = packageBean.find(id);

        if (aPackage != null) {
            return Response.ok(toDTO(aPackage)).build();
        }
        return Response.status(Response.Status.NOT_FOUND)
                .entity("ERROR_FINDING_PACKAGE")
                .build();
    }

    @GET
    @Path("{id}/products")
    @Authenticated
    @RolesAllowed({"LogisticsOperator"})
    public Response getPackageProducts(@PathParam("id") long id) throws MyEntityNotFoundException{
        Package aPackage = packageBean.getPackageProducts(id);
        if (aPackage != null) {
            var dtos = productsToDTOs(aPackage.getProducts());
            return Response.ok(dtos).build();
        }
        return Response.status(Response.Status.NOT_FOUND)
                .entity("ERROR_FINDING_PACKAGE")
                .build();
    }

    @GET
    @Path("{id}/sensors")
    @Authenticated
    @RolesAllowed({"LogisticsOperator"})
    public Response getPackageSensors(@PathParam("id") long id) throws MyEntityNotFoundException{
        Package aPackage = packageBean.getPackageSensors(id);
        if (aPackage != null) {
            var dtos = sensorsToDTOs(aPackage.getSensors());
            return Response.ok(dtos).build();
        }
        return Response.status(Response.Status.NOT_FOUND)
                .entity("ERROR_FINDING_PACKAGE")
                .build();
    }


    //TODO: swap id for code which can be inserted->unique
    @POST
    @Path("/")
    @RolesAllowed({"LogisticsOperator"})
    public Response create(PackageDTO packageDTO)
            throws MyEntityExistsException, MyEntityNotFoundException, MyConstraintViolationException {
        long packageId = packageBean.create(
                packageDTO.getMaterial(),
                packageDTO.getPackageType()
        );
        var aPackage = packageBean.find(packageId);
        return Response.status(Response.Status.CREATED).entity(toDTO(aPackage)).build();
    }

    @PUT
    @Path("{id}")
    @Authenticated
    @RolesAllowed({"LogisticsOperator"})
    public Response update(@PathParam("id") long id, PackageDTO packageDTO)
            throws MyEntityNotFoundException, MyConstraintViolationException {

        packageBean.update(
                id,
                packageDTO.getMaterial(),
                packageDTO.getPackageType()
        );
        var aPackage = packageBean.find(id);
        return Response.ok(toDTO(aPackage)).build();
    }

    @DELETE
    @Path("{id}")
    @Authenticated
    @RolesAllowed({"LogisticsOperator"})
    public Response delete(@PathParam("id") long id) throws MyEntityNotFoundException{
        Package aPackage = packageBean.delete(id);
        return Response.status(Response.Status.OK).entity(toDTO(aPackage)).build();
    }
}
