package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.ws;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.assemblers.DeliveryAssembler;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.dtos.DeliveryDTO;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.ejbs.DeliveryBean;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.enums.DeliveryStatus;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.*;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.pagination.PaginationMetadata;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.pagination.PaginationResponse;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.security.Authenticated;

import java.util.HashMap;
import java.util.Map;


@Path("deliveries")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class DeliveryService {
    @EJB
    private DeliveryBean deliveryBean;

    @Context
    private SecurityContext securityContext;

    @GET
    @Path("/all")
    @Authenticated
    @RolesAllowed({"LogisticsOperator"})
    public Response getAll(@DefaultValue("1") @QueryParam("page") int page,
                           @DefaultValue("10") @QueryParam("pageSize") int pageSize
    ) throws IllegalArgumentException {

        Map<String, String> filterMap = new HashMap<>();

        var products = deliveryBean.getDeliveries(filterMap, page, pageSize);
        var dtos = DeliveryAssembler.fromNoPackages(products);
        long totalItems = deliveryBean.getDeliveriesCount(filterMap);
        long totalPages = (totalItems + pageSize - 1) / pageSize;
        PaginationMetadata paginationMetadata = new PaginationMetadata(page, pageSize, totalItems, totalPages, dtos.size());
        PaginationResponse<DeliveryDTO> paginationResponse = new PaginationResponse<>(dtos, paginationMetadata);
        return Response.ok(paginationResponse).build();
    }

    @GET
    @Path("{id}")
    @Authenticated
    @RolesAllowed({"LogisticsOperator", "Customer"})
    public Response get(@PathParam("id") long id) throws MyEntityNotFoundException {
        var delivery = deliveryBean.findWithPackages(id);
        return Response.ok(DeliveryAssembler.from(delivery)).build();
    }

    @POST
    @Path("/")
    @Authenticated
    @RolesAllowed({"LogisticsOperator"})
    public Response create(DeliveryDTO deliveryDTO) throws MyIllegalConstraintException, MyConstraintViolationException, MyEntityNotFoundException, MyEntityExistsException, MyValidationException {
        var principal = securityContext.getUserPrincipal();

        var id = deliveryBean.create(
                principal.getName(),
                deliveryDTO.getOrderId(),
                deliveryDTO.getPackageCodes()
        );

        var delivery = deliveryBean.find(id);
        return Response.status(Response.Status.CREATED).entity(DeliveryAssembler.fromNoPackages(delivery)).build();
    }

    @PATCH
    @Path("{id}/update-status")
    @Authenticated
    @RolesAllowed({"LogisticsOperator"})
    public Response updateStatus(@PathParam("id") Long id, DeliveryStatus deliveryStatus) throws MyEntityNotFoundException, MyIllegalConstraintException, MyConstraintViolationException {
        deliveryBean.updateStatus(id, deliveryStatus);
        return Response.status(Response.Status.OK).build();
    }
}
