package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.ws;

import jakarta.ejb.EJB;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.SecurityContext;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.assemblers.PrimaryPackageMeasurementUnitAssembler;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.assemblers.ProductCategoryAssembler;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.dtos.PrimaryPackageMeasurementUnitDTO;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.dtos.ProductCategoryDTO;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.ejbs.ProductCategoryBean;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.security.Authenticated;

import java.util.List;

@Path("product-categories")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class ProductCategoryService {
    @EJB
    private ProductCategoryBean productCategoryBean;

    @Context
    private SecurityContext securityContext;

    @GET
    @Path("/all")
    @Authenticated
    public List<ProductCategoryDTO> getAll(){
        return ProductCategoryAssembler.from(productCategoryBean.getCategories());
    }
}
