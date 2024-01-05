package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.ws;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.dtos.*;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.ejbs.ProductBean;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.*;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.Package;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.security.Authenticated;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
                product.getContainerStock(),
                product.getPrimaryPackQuantity(),
                product.getSecondaryPackQuantity(),
                product.getTertiaryPackQuantity()
        );
    }

    private List<ProductDTO> toDTOs(List<Product> products) {
        return products.stream().map(this::toDTO).collect(Collectors.toList());
    }

    private ProductParameterDTO productParametertoDTO(ProductParameter productParameter) {
        return new ProductParameterDTO(
                productParameter.getId(),
                productParameter.getProduct().getId(),
                productParameter.getSensorType().getId(),
                productParameter.getMinValue(),
                productParameter.getMaxValue()
        );
    }

    private List<ProductParameterDTO> productParameterstoDTOs(List<ProductParameter> productParameters) {
        return productParameters.stream().map(this::productParametertoDTO).collect(Collectors.toList());
    }

    private SensorTypeDTO sensorTypetoDTO(SensorType sensorType){
        return new SensorTypeDTO(
                sensorType.getId(),
                sensorType.getName(),
                sensorType.getMeasurementUnit()
        );
    }

    private PackageDTO packageToDTO(Package aPackage){
        return new PackageDTO(
                aPackage.getCode(),
                aPackage.getMaterial(),
                aPackage.getPackageType()
        );
    }

    private List<PackageDTO> packagesToDTOs(List<Package> packages) {
        return packages.stream().map(this::packageToDTO).collect(Collectors.toList());
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
        if (!productBean.exists(id)){
            throw new MyEntityNotFoundException("The product with the id: " + id + " does not exist");
        }

        var productParameters = productBean.getProductParametersWithSensorType(id);
        List<ProductParameterDTO> dtos = new ArrayList<>();

        for (ProductParameter pp : productParameters) {
            ProductParameterDTO dto = new ProductParameterDTO(
                    pp.getId(),
                    id,
                    pp.getSensorType().getId(),
                    pp.getMinValue(),
                    pp.getMaxValue(),
                    sensorTypetoDTO(pp.getSensorType())
            );
            dtos.add(dto);
        }

        return Response.ok(dtos).build();
    }

    @GET
    @Path("{id}/packages")
    @Authenticated
    @RolesAllowed({"LogisticsOperator"})
    public Response getProductPackages(@PathParam("id") long id) throws MyEntityNotFoundException{
        Product product = productBean.getProductPackages(id);
        if (product != null) {
            var dtos = packagesToDTOs(product.getPackages());
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
                productDTO.getPrimaryPackQuantity(),
                productDTO.getSecondaryPackQuantity(),
                productDTO.getTertiaryPackQuantity()
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
                productDTO.getReference(),
                productDTO.getPrimaryPackQuantity(),
                productDTO.getSecondaryPackQuantity(),
                productDTO.getTertiaryPackQuantity()
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

    @DELETE
    @Path("{id}")
    @Authenticated
    @RolesAllowed({"Manufacturer"})
    public Response delete(@PathParam("id") long id) throws MyEntityNotFoundException{
        var product = productBean.delete(id);
        return Response.status(Response.Status.OK).entity(toDTO(product)).build();
    }
}
