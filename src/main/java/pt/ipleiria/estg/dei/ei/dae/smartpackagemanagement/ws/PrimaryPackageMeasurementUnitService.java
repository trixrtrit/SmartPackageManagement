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
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.assemblers.PrimaryPackageTypeAssembler;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.dtos.PrimaryPackageMeasurementUnitDTO;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.dtos.PrimaryPackageTypeDTO;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.ejbs.PrimaryPackageMeasurementUnitBean;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.ejbs.PrimaryPackageTypeBean;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.PrimaryPackageMeasurementUnit;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.security.Authenticated;

import java.util.List;

@Path("primary-package-units")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class PrimaryPackageMeasurementUnitService {
    @EJB
    private PrimaryPackageMeasurementUnitBean primaryPackageMeasurementUnitBean;

    @Context
    private SecurityContext securityContext;

    @GET
    @Path("/all")
    @Authenticated
    public List<PrimaryPackageMeasurementUnitDTO> getAll(){
        return PrimaryPackageMeasurementUnitAssembler.from(primaryPackageMeasurementUnitBean.getUnits());
    }
}
