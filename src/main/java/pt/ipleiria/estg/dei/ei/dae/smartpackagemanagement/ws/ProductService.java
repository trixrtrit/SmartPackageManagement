package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.ws;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.dtos.ProductDTO;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.ejbs.ProductBean;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.Product;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.security.Authenticated;

import java.util.List;
import java.util.stream.Collectors;

@Path("products")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class ProductService {
    @EJB
    private ProductBean productBean;

    //TODO: adicionar DTO de orderItems
    private ProductDTO toDTO(Product product) {
        return new ProductDTO(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.isActive(),
                product.getStock(),
                product.getManufacturer().getUsername(),
                product.getaPackage().getId()
        );
    }

    private List<ProductDTO> toDTOs(List<Product> products) {
        return products.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @GET
    @Path("/all")
    public List<ProductDTO> getAll() {
        return toDTOs(productBean.getProducts());
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    public Response get(@PathParam("id") long id) throws MyEntityNotFoundException {
        Product product = productBean.find(id);

        if (product != null) {
            return Response.ok(toDTO(product)).build();
        }
        return Response.status(Response.Status.NOT_FOUND)
                .entity("ERROR_FINDING_PRODUCT")
                .build();
    }

    //TODO: endpoint devia ser /manufacturers/{username}/products?
    @POST
    @Path("/")
    @Authenticated
    @RolesAllowed({"Manufacturer"})
    public Response create(ProductDTO productDTO)
            throws MyEntityExistsException, MyEntityNotFoundException, MyConstraintViolationException {
        productBean.create(
                productDTO.getName(),
                productDTO.getDescription(),
                productDTO.getPrice(),
                productDTO.getManufacturerUsername(),
                productDTO.getPackageId(),
                productDTO.getStock()
        );
        var product = productBean.find(productDTO.getPackageId());
        return Response.status(Response.Status.CREATED).entity(toDTO(product)).build();
    }

    @PUT
    @Path("{id}")
    @Authenticated
    @RolesAllowed({"Manufacturer"})
    public Response update(@PathParam("id") long id, ProductDTO productDTO) throws MyEntityNotFoundException {
        productBean.update(
                id,
                productDTO.getName(),
                productDTO.getDescription(),
                productDTO.getPrice(),
                productDTO.getStock()
        );
        var product = productBean.find(id);
        return Response.ok(toDTO(product)).build();
    }

    @DELETE
    @Path("{id}")
    @Authenticated
    @RolesAllowed({"Manufacturer"})
    public Response delete(@PathParam("id") long id) throws MyEntityNotFoundException{
        var product = productBean.delete(id);
        return Response.status(Response.Status.OK).entity(toDTO(product)).build();
    }
}
