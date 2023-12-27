package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.ws;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.dtos.ProductParameterDTO;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.ejbs.ProductParameterBean;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.ProductParameter;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.security.Authenticated;

import java.util.List;
import java.util.stream.Collectors;

@Path("product-parameters")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class ProductParameterService {
    @EJB
    private ProductParameterBean productParameterBean;

    @Context
    private SecurityContext securityContext;

    private ProductParameterDTO toDTO(ProductParameter productParameter) {
        return new ProductParameterDTO(
                productParameter.getProduct().getId(),
                productParameter.getSensorType().getId(),
                productParameter.getMinValue(),
                productParameter.getMaxValue()
        );
    }

    private List<ProductParameterDTO> toDTOs(List<ProductParameter> productParameters) {
        return productParameters.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @GET
    @Path("/all")
    public List<ProductParameterDTO> getAll() {
        return toDTOs(productParameterBean.getProductParameters());
    }

    @POST
    @Path("/")
    @Authenticated
    @RolesAllowed({"Manufacturer"})
    public Response create(ProductParameterDTO productParameterDTO)
            throws MyEntityExistsException, MyEntityNotFoundException, MyConstraintViolationException {
        var key = productParameterBean.create(
                productParameterDTO.getProductId(),
                productParameterDTO.getSensorTypeId(),
                productParameterDTO.getMinValue(),
                productParameterDTO.getMaxValue()
        );
        var productParameter = productParameterBean.find(key);
        return Response.status(Response.Status.CREATED).entity(toDTO(productParameter)).build();
    }
}