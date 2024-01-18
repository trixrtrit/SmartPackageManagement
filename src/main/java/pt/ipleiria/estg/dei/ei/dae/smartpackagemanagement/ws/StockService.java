package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.ws;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.assemblers.StockAssembler;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.dtos.PackageDTO;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.ejbs.StockBean;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyValidationException;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.security.Authenticated;

import java.util.ArrayList;

import java.util.List;

@Path("stocks")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class StockService {
    @EJB
    private StockBean stockBean;

    @Context
    private SecurityContext securityContext;

    @PUT
    @Path("{id}/add-primary-or-secondary")
    @Authenticated
    @RolesAllowed({"LogisticsOperator", "Manufacturer"})
    public Response addPrimaryOrSecondaryPackage(@PathParam("id") long id, PackageDTO aPackage1, PackageDTO aPackage2)
            throws MyEntityNotFoundException, MyValidationException {

        if(aPackage1 != null) {
            stockBean.addPrimaryPackage(
                    id,
                    aPackage1.getCode()
            );
        }
        if(aPackage2 != null) {
            stockBean.addSecondaryPackage(
                    id,
                    aPackage2.getCode()
            );
        }
        var stock = stockBean.find(id);
        return Response.ok(StockAssembler.fromWithPackages(stock)).build();
    }

    @PUT
    @Path("{id}/remove-primary-or-secondary")
    @Authenticated
    @RolesAllowed({"LogisticsOperator", "Manufacturer"})
    public Response removePrimaryOrSecondaryPackage(@PathParam("id") long id, PackageDTO aPackage1, PackageDTO aPackage2)
            throws MyEntityNotFoundException, MyValidationException {

        if(aPackage1 != null) {
            stockBean.removePrimaryPackage(
                    id,
                    aPackage1.getCode()
            );
        }
        if(aPackage2 != null) {
            stockBean.removeSecondaryPackage(
                    id,
                    aPackage2.getCode()
            );
        }
        var stock = stockBean.find(id);
        return Response.ok(StockAssembler.fromWithPackages(stock)).build();
    }

    @PUT
    @Path("{id}/add-primaries-or-secondaries")
    @Authenticated
    @RolesAllowed({"LogisticsOperator", "Manufacturer"})
    public Response addPrimaryOrSecondaryPackages(@PathParam("id") long id, List<PackageDTO> aPackages1, List<PackageDTO> aPackages2)
            throws MyEntityNotFoundException, MyValidationException {

        if (aPackages1 != null && !aPackages1.isEmpty()) {
            List<Long> codes = new ArrayList<>();
            for (PackageDTO packageDTO : aPackages1) {
                Long code = packageDTO.getCode();
                codes.add(code);
            }
            stockBean.addPrimaryPackageList(id, codes);
        }
        if (aPackages2 != null && !aPackages2.isEmpty()) {
            List<Long> codes = new ArrayList<>();
            for (PackageDTO packageDTO : aPackages2) {
                Long code = packageDTO.getCode();
                codes.add(code);
            }
            stockBean.addSecondaryPackageList(id, codes);
        }
        var stock = stockBean.find(id);
        return Response.ok(StockAssembler.fromWithPackages(stock)).build();
    }

    @PUT
    @Path("{id}/remove-primaries-or-secondaries-from")
    @Authenticated
    @RolesAllowed({"LogisticsOperator", "Manufacturer"})
    public Response removePrimaryOrSecondaryPackages(@PathParam("id") long id, List<PackageDTO> aPackages1, List<PackageDTO> aPackages2)
            throws MyEntityNotFoundException, MyValidationException {

        if (aPackages1 != null && !aPackages1.isEmpty()) {
            List<Long> codes = new ArrayList<>();
            for (PackageDTO packageDTO : aPackages1) {
                Long code = packageDTO.getCode();
                codes.add(code);
            }
            stockBean.removePrimaryPackageList(id, codes);
        }
        if (aPackages2 != null && !aPackages2.isEmpty()) {
            List<Long> codes = new ArrayList<>();
            for (PackageDTO packageDTO : aPackages2) {
                Long code = packageDTO.getCode();
                codes.add(code);
            }
            stockBean.removeSecondaryPackageList(id, codes);
        }
        var stock = stockBean.find(id);
        return Response.ok(StockAssembler.fromWithPackages(stock)).build();
    }


}
