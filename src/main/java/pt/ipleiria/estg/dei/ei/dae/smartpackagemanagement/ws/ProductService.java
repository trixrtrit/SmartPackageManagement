package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.ws;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.assemblers.CustomerAssembler;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.assemblers.PackageAssembler;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.assemblers.ProductAssembler;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.assemblers.ProductParameterAssembler;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.dtos.*;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.ejbs.ProductBean;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.*;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.pagination.PaginationMetadata;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.pagination.PaginationResponse;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.security.Authenticated;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.specifications.GenericFilterMapBuilder;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("products")
@Transactional
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class ProductService {
    @EJB
    private ProductBean productBean;

    //TODO: adicionar DTO de orderItems
    @GET
    @Path("/all")
    public Response getAll(@QueryParam("reference") String reference,
                           @QueryParam("name") String name,
                           @QueryParam("description") String description,
                           @QueryParam("minPrice") double minPrice,
                           @QueryParam("maxPrice") double maxPrice,
                           @DefaultValue("1") @QueryParam("page") int page,
                           @DefaultValue("10") @QueryParam("pageSize") int pageSize
    ) throws IllegalArgumentException {

        Map<String, String> filterMap = new HashMap<>();
        GenericFilterMapBuilder.addToFilterMap(reference, filterMap, "productReference", "");
        GenericFilterMapBuilder.addToFilterMap(name, filterMap, "name", "");
        GenericFilterMapBuilder.addToFilterMap(description, filterMap, "description", "");
        GenericFilterMapBuilder.addToFilterMap(minPrice, filterMap, "price", "gte");
        GenericFilterMapBuilder.addToFilterMap(maxPrice, filterMap, "price", "lte");

        var dtos = ProductAssembler.from(productBean.getProducts(filterMap, page, pageSize));
        long totalItems = productBean.getProductsCount(filterMap);
        long totalPages = (totalItems + pageSize - 1) / pageSize;
        PaginationMetadata paginationMetadata = new PaginationMetadata(page, pageSize, totalItems, totalPages, dtos.size());
        PaginationResponse<ProductDTO> paginationResponse = new PaginationResponse<>(dtos, paginationMetadata);
        return Response.ok(paginationResponse).build();
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    public Response get(@PathParam("id") long id) throws MyEntityNotFoundException {
        Product product = productBean.find(id);

        if (product != null) {
            return Response.ok(ProductAssembler.from(product)).build();
        }
        return Response.status(Response.Status.NOT_FOUND)
                .entity("ERROR_FINDING_PRODUCT")
                .build();
    }

    @GET
    @Path("{id}/product-parameters")
    public Response getProductProductParameters(@PathParam("id") Long id) throws MyEntityNotFoundException {
        if (!productBean.exists(id)){
            throw new MyEntityNotFoundException("The product with the id: " + id + " does not exist");
        }

        var productParameters = productBean.getProductParametersWithSensorType(id);
        var dtos = ProductParameterAssembler.fromWithSensorType(productParameters);
        return Response.ok(dtos).build();
    }

    @GET
    @Path("{id}/packages")
    @Authenticated
    @RolesAllowed({"LogisticsOperator"})
    public Response getProductPackages(@PathParam("id") long id) throws MyEntityNotFoundException{
        Product product = productBean.getProductPackages(id);
        if (product != null) {
            var dtos = PackageAssembler.from(product.getPackages());
            return Response.ok(dtos).build();
        }
        return Response.status(Response.Status.NOT_FOUND)
                .entity("ERROR_FINDING_PRODUCT")
                .build();
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
                productDTO.getReference(),
                productDTO.getPrimaryPackageMeasurementUnitId(),
                productDTO.getPrimaryPackageTypeId(),
                productDTO.getPrimaryPackQuantity(),
                productDTO.getSecondaryPackQuantity(),
                productDTO.getTertiaryPackQuantity()
        );
        var product = productBean.find(productId);
        return Response.status(Response.Status.CREATED).entity(ProductAssembler.from(product)).build();
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
                productDTO.getReference(),
                productDTO.getPrimaryPackQuantity(),
                productDTO.getSecondaryPackQuantity(),
                productDTO.getTertiaryPackQuantity()
        );
        var product = productBean.find(id);
        return Response.ok(ProductAssembler.from(product)).build();
    }

    @PUT
    @Path("{id}/active-status")
    @Authenticated
    @RolesAllowed({"Manufacturer"})
    public Response changeActiveStatus(@PathParam("id") long id)
            throws MyEntityNotFoundException {
        productBean.changeActiveStatus(id);
        var product = productBean.find(id);
        return Response.ok(ProductAssembler.from(product)).build();
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
        return Response.ok(ProductAssembler.from(product)).build();
    }

    @DELETE
    @Path("{id}")
    @Authenticated
    @RolesAllowed({"Manufacturer"})
    public Response delete(@PathParam("id") long id) throws MyEntityNotFoundException{
        var product = productBean.delete(id);
        return Response.status(Response.Status.OK).entity(ProductAssembler.from(product)).build();
    }
}
