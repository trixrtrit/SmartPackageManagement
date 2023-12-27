package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.ws;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.dtos.ProductDTO;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.dtos.ProductParameterDTO;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.dtos.ProductStockDTO;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.ejbs.ProductBean;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.Product;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.ProductParameter;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.security.Authenticated;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Path("products")
@Transactional
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
                product.getManufacturer().getUsername(),
                product.getProductReference(),
                product.getUnitStock(),
                product.getBoxStock(),
                product.getContainerStock()
        );
    }

    private List<ProductDTO> toDTOs(List<Product> products) {
        return products.stream().map(this::toDTO).collect(Collectors.toList());
    }


    private ProductParameterDTO productParametertoDTO(ProductParameter productParameter) {
        return new ProductParameterDTO(
                productParameter.getProduct().getId(),
                productParameter.getSensorType().getId(),
                productParameter.getMinValue(),
                productParameter.getMaxValue()
        );
    }

    private List<ProductParameterDTO> productParameterstoDTOs(List<ProductParameter> productParameters) {
        return productParameters.stream().map(this::productParametertoDTO).collect(Collectors.toList());
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

    @GET
    @Path("{id}/product-parameters")
    public Response getProductProductParameters(@PathParam("id") Long id) throws MyEntityNotFoundException {
        var product = productBean.find(id);
        var dtos = productParameterstoDTOs(product.getProductParameters());
        return Response.ok(dtos).build();
    }

    @POST
    @Path("/")
    @Authenticated
    @RolesAllowed({"Manufacturer"})
    public Response create(ProductDTO productDTO)
            throws MyEntityExistsException, MyEntityNotFoundException, MyConstraintViolationException {
        long productId = productBean.create(
                productDTO.getName(),
                productDTO.getDescription(),
                productDTO.getPrice(),
                productDTO.getManufacturerUsername(),
                productDTO.getReference()
        );
        var product = productBean.find(productId);
        return Response.status(Response.Status.CREATED).entity(toDTO(product)).build();
    }

    @GET
    @Path("export")
    @Produces("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    //@Authenticated
    //@RolesAllowed({"Manufacturer"})
    public Response export(@QueryParam("fileLocation") String fileLocation)
            throws IOException {
        try {
            String location = productBean.export(fileLocation);
            File file = new File(location);
            Response.ResponseBuilder response = Response.ok(file);
            response.header("Content-Disposition", "attachment;filename=temp.xlsx");
            return response.build();
        } catch (IOException e) {
            e.printStackTrace();
            return Response.serverError().build();
        }
    }

    @PUT
    @Path("{id}")
    @Authenticated
    @RolesAllowed({"Manufacturer"})
    public Response update(@PathParam("id") long id, ProductDTO productDTO)
            throws MyEntityNotFoundException, MyConstraintViolationException {
        productBean.update(
                id,
                productDTO.getName(),
                productDTO.getDescription(),
                productDTO.getPrice(),
                productDTO.getReference()
        );
        var product = productBean.find(id);
        return Response.ok(toDTO(product)).build();
    }

    @PUT
    @Path("{id}/active-status")
    @Authenticated
    @RolesAllowed({"Manufacturer"})
    public Response changeActiveStatus(@PathParam("id") long id)
            throws MyEntityNotFoundException {
        productBean.changeActiveStatus(id);
        var product = productBean.find(id);
        return Response.ok(toDTO(product)).build();
    }

    @PUT
    @Path("{id}/set-stock")
    @Authenticated
    @RolesAllowed({"LogisticsOperator"})
    public Response setStocks(@PathParam("id") long id, ProductStockDTO productStockDTO) throws MyEntityNotFoundException {
        productBean.setStocks(
                id,
                productStockDTO.getUnitStock(),
                productStockDTO.getBoxStock(),
                productStockDTO.getContainerStock()
        );
        var product = productBean.find(id);
        return Response.ok(toDTO(product)).build();
    }

    @PUT
    @Path("{id}/set-package")
    @Authenticated
    @RolesAllowed({"LogisticsOperator"})
    public Response setPackage(@PathParam("id") long id, long packageId)
            throws MyEntityNotFoundException {
        productBean.setPackage(id, packageId);
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
