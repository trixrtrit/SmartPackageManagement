package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.ws;

import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.SecurityContext;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.assemblers.PrimaryPackageTypeAssembler;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.dtos.PrimaryPackageTypeDTO;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.ejbs.PrimaryPackageTypeBean;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.security.Authenticated;

import java.util.List;

@Path("primary-package-types")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class PrimaryPackageTypeService {
    @EJB
    private PrimaryPackageTypeBean primaryPackageTypeBean;

    @Context
    private SecurityContext securityContext;

    @GET
    @Path("/all")
    @Authenticated
    public List<PrimaryPackageTypeDTO> getAll(){
        return PrimaryPackageTypeAssembler.from(primaryPackageTypeBean.getTypes());
    }
}
