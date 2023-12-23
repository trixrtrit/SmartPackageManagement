package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.ws;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.dtos.*;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.ejbs.ManufacturerBean;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.Manufacturer;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.Package;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.Product;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.security.Authenticated;

import java.util.List;
import java.util.stream.Collectors;

@Path("manufacturers")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class ManufacturerService {
    @EJB
    private ManufacturerBean manufacturerBean;

    @Context
    private SecurityContext securityContext;

    private ManufacturerDTO toDTO(Manufacturer manufacturer) {
        return new ManufacturerDTO(
                manufacturer.getUsername(),
                manufacturer.getPassword(),
                manufacturer.getEmail(),
                manufacturer.getName(),
                packagesToDTOs(manufacturer.getPackages()),
                productsToDTOs(manufacturer.getProducts())
        );
    }

    private ManufacturerDTO toDTOnoProductsNoPackages(Manufacturer manufacturer) {
        return new ManufacturerDTO(
                manufacturer.getUsername(),
                manufacturer.getPassword(),
                manufacturer.getEmail(),
                manufacturer.getName()
        );
    }

    private ManufacturerDTO toDTOnoProducts(Manufacturer manufacturer) {
        return new ManufacturerDTO(
                manufacturer.getUsername(),
                manufacturer.getPassword(),
                manufacturer.getEmail(),
                manufacturer.getName(),
                packagesToDTOs(manufacturer.getPackages()),
                false
        );
    }

    private ManufacturerDTO toDTOnoPackages(Manufacturer manufacturer) {
        return new ManufacturerDTO(
                manufacturer.getUsername(),
                manufacturer.getPassword(),
                manufacturer.getEmail(),
                manufacturer.getName(),
                productsToDTOs(manufacturer.getProducts())
        );
    }

    private PackageDTO packageToDTO(Package aPackage) {
        return new PackageDTO(
                aPackage.getId(),
                aPackage.getMaterial(),
                aPackage.getType()
        );
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

    private List<ManufacturerDTO> toDTOs(List<Manufacturer> manufacturers) {
        return manufacturers.stream().map(this::toDTO).collect(Collectors.toList());
    }

    private List<ManufacturerDTO> toDTOsNoProductsNoPackages(List<Manufacturer> manufacturers) {
        return manufacturers.stream().map(this::toDTOnoProductsNoPackages).collect(Collectors.toList());
    }

    private List<ManufacturerDTO> toDTOsNoProducts(List<Manufacturer> manufacturers) {
        return manufacturers.stream().map(this::toDTOnoProducts).collect(Collectors.toList());
    }

    private List<ManufacturerDTO> toDTOsNoPackages(List<Manufacturer> manufacturers) {
        return manufacturers.stream().map(this::toDTOnoPackages).collect(Collectors.toList());
    }

    private List<PackageDTO> packagesToDTOs(List<Package> packages) {
        return packages.stream().map(this::packageToDTO).collect(Collectors.toList());
    }

    private List<ProductDTO> productsToDTOs(List<Product> products) {
        return products.stream().map(this::productToDTO).collect(Collectors.toList());
    }

    @GET
    @Path("/all")
    public List<ManufacturerDTO> getAll() {
        return toDTOsNoProductsNoPackages(manufacturerBean.getManufacturers());
    }

    @GET
    @Path("{username}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    @Authenticated
    @RolesAllowed({"Manufacturer"})
    public Response get(@PathParam("username") String username) throws MyEntityNotFoundException {
        var principal = securityContext.getUserPrincipal();

        if (!principal.getName().equals(username)){
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        Manufacturer manufacturer = manufacturerBean.find(username);

        if (manufacturer != null) {
            return Response.ok(toDTO(manufacturer)).build();
        }
        return Response.status(Response.Status.NOT_FOUND)
                .entity("ERROR_FINDING_MANUFACTURER")
                .build();
    }

    @GET
    @Path("{username}/products")
    @Authenticated
    @RolesAllowed({"Customer", "Manufacturer"})
    public Response getManufacturerProducts(@PathParam("username") String username) throws MyEntityNotFoundException{
        var principal = securityContext.getUserPrincipal();
        if (!principal.getName().equals(username)){
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        Manufacturer manufacturer = manufacturerBean.getManufacturerProducts(username);
        if (manufacturer != null) {
            var dtos = productsToDTOs(manufacturer.getProducts());
            return Response.ok(dtos).build();
        }
        return Response.status(Response.Status.NOT_FOUND)
                .entity("ERROR_FINDING_MANUFACTURER")
                .build();
    }

    @GET
    @Path("{username}/packages")
    @Authenticated
    @RolesAllowed({"Customer", "Manufacturer"})
    public Response getManufacturerPackages(@PathParam("username") String username) throws MyEntityNotFoundException{
        var principal = securityContext.getUserPrincipal();
        if (!principal.getName().equals(username)){
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        Manufacturer manufacturer = manufacturerBean.getManufacturerPackages(username);
        if (manufacturer != null) {
            var dtos = packagesToDTOs(manufacturer.getPackages());
            return Response.ok(dtos).build();
        }
        return Response.status(Response.Status.NOT_FOUND)
                .entity("ERROR_FINDING_MANUFACTURER")
                .build();
    }

    @POST
    @Path("/")
    public Response create(ManufacturerDTO manufacturerDTO)
            throws MyEntityExistsException, MyEntityNotFoundException, MyConstraintViolationException {
        manufacturerBean.create(
                manufacturerDTO.getUsername(),
                manufacturerDTO.getPassword(),
                manufacturerDTO.getName(),
                manufacturerDTO.getEmail()
        );
        var manufacturer = manufacturerBean.find(manufacturerDTO.getUsername());
        return Response.status(Response.Status.CREATED).entity(toDTOnoProductsNoPackages(manufacturer)).build();
    }

    @PUT
    @Path("{username}")
    @Authenticated
    @RolesAllowed({"Manufacturer"})
    public Response update(@PathParam("username") String username, ManufacturerDTO manufacturerDTO) throws MyEntityNotFoundException {
        var principal = securityContext.getUserPrincipal();
        if (!principal.getName().equals(username)){
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        manufacturerBean.update(
                username,
                manufacturerDTO.getName(),
                manufacturerDTO.getEmail()
        );
        var manufacturer = manufacturerBean.find(username);
        return Response.ok(toDTOnoProductsNoPackages(manufacturer)).build();
    }

    @DELETE
    @Path("{username}")
    @Authenticated
    @RolesAllowed({"Manufacturer"})
    public Response delete(@PathParam("username") String username) throws MyEntityNotFoundException{
        var principal = securityContext.getUserPrincipal();
        if (!principal.getName().equals(username)){
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        Manufacturer manufacturer = manufacturerBean.delete(username);
        return Response.status(Response.Status.OK).entity(toDTO(manufacturer)).build();
    }
}
