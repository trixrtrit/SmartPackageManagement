package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.ws;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.mail.MessagingException;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.assemblers.ManufacturerAssembler;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.assemblers.NotificationAssembler;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.assemblers.ProductAssembler;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.dtos.*;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.ejbs.EmailBean;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.ejbs.ManufacturerBean;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.ejbs.NotificationBean;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.ejbs.ProductBean;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.Manufacturer;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.pagination.PaginationMetadata;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.pagination.PaginationResponse;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.security.Authenticated;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.specifications.GenericFilterMapBuilder;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.utils.DateUtil;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Path("manufacturers")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class ManufacturerService {
    @EJB
    private ManufacturerBean manufacturerBean;
    @EJB
    private ProductBean productBean;
    @EJB
    private NotificationBean notificationBean;
    @EJB
    private EmailBean emailBean;
    @Context
    private SecurityContext securityContext;

    @GET
    @Path("/all")
    @RolesAllowed({"LogisticsOperator"})
    public Response getAll(@QueryParam("username") String username,
                           @QueryParam("name") String name,
                           @QueryParam("email") String email,
                           @DefaultValue("1") @QueryParam("page") int page,
                           @DefaultValue("10") @QueryParam("pageSize") int pageSize
    ) throws IllegalArgumentException {

        Map<String, String> filterMap = new HashMap<>();
        GenericFilterMapBuilder.addToFilterMap(username, filterMap, "username", "");
        GenericFilterMapBuilder.addToFilterMap(name, filterMap, "name", "");
        GenericFilterMapBuilder.addToFilterMap(email, filterMap, "email", "");

        var dtos = ManufacturerAssembler.from(manufacturerBean.getManufacturers(filterMap, page, pageSize));
        long totalItems = manufacturerBean.getManufacturersCount(filterMap);
        long totalPages = (totalItems + pageSize - 1) / pageSize;
        PaginationMetadata paginationMetadata = new PaginationMetadata(page, pageSize, totalItems, totalPages, dtos.size());
        PaginationResponse<ManufacturerDTO> paginationResponse = new PaginationResponse<>(dtos, paginationMetadata);
        return Response.ok(paginationResponse).build();
    }

    @GET
    @Path("{username}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    @Authenticated
    @RolesAllowed({"Manufacturer"})
    public Response get(@PathParam("username") String username) throws MyEntityNotFoundException {
        var principal = securityContext.getUserPrincipal();

        if (!principal.getName().equals(username)){
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        Manufacturer manufacturer = manufacturerBean.find(username);

        if (manufacturer != null) {
            return Response.ok(ManufacturerAssembler.fromWithProducts(manufacturer)).build();
        }
        return Response.status(Response.Status.NOT_FOUND)
                .entity("ERROR_FINDING_MANUFACTURER")
                .build();
    }

    @GET
    @Path("{username}/products")
    @Authenticated
    @RolesAllowed({"LogisticsOperator", "Manufacturer"})
    public Response getManufacturerProducts(
            @PathParam("username") String username,
            @QueryParam("reference") String reference,
            @QueryParam("name") String name,
            @QueryParam("description") String description,
            @QueryParam("category") String category,
            @QueryParam("minPrice") double minPrice,
            @QueryParam("maxPrice") double maxPrice,
            @DefaultValue("1") @QueryParam("page") int page,
            @DefaultValue("10") @QueryParam("pageSize") int pageSize
    ) throws MyEntityNotFoundException{
        var principal = securityContext.getUserPrincipal();
        if (securityContext.isUserInRole("Manufacturer") && !principal.getName().equals(username)){
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        Map<String, String> filterMap = new HashMap<>();
        GenericFilterMapBuilder.addToFilterMap(username, filterMap, "manufacturer.username", "");
        GenericFilterMapBuilder.addToFilterMap(reference, filterMap, "productReference", "");
        GenericFilterMapBuilder.addToFilterMap(name, filterMap, "name", "");
        GenericFilterMapBuilder.addToFilterMap(description, filterMap, "description", "");
        GenericFilterMapBuilder.addToFilterMap(minPrice, filterMap, "price", "gte");
        GenericFilterMapBuilder.addToFilterMap(maxPrice, filterMap, "price", "lte");
        GenericFilterMapBuilder.addToFilterMap(category, filterMap, "productCategory.category", "");

        Manufacturer manufacturer = manufacturerBean.find(username);
        if (manufacturer != null) {
            var dtos = ProductAssembler.from(productBean.getProducts(filterMap, page, pageSize));
            long totalItems = productBean.getProductsCount(filterMap);
            long totalPages = (totalItems + pageSize - 1) / pageSize;
            PaginationMetadata paginationMetadata = new PaginationMetadata(page, pageSize, totalItems, totalPages, dtos.size());
            PaginationResponse<ProductDTO> paginationResponse = new PaginationResponse<>(dtos, paginationMetadata);
            return Response.ok(paginationResponse).build();
        }
        return Response.status(Response.Status.NOT_FOUND)
                .entity("ERROR_FINDING_MANUFACTURER")
                .build();
    }

    @GET
    @Path("{username}/notifications")
    @Authenticated
    @RolesAllowed({"Manufacturer"})
    public Response getNotifications(
            @PathParam("username") String username,
            @QueryParam("text") String text,
            @QueryParam("startTime") Long startTime,
            @QueryParam("endTime") Long endTime,
            @DefaultValue("1") @QueryParam("page") int page,
            @DefaultValue("10") @QueryParam("pageSize") int pageSize) {
        var principal = securityContext.getUserPrincipal();

        if (securityContext.isUserInRole("Manufacturer") && !principal.getName().equals(username)) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        Date[] dates = DateUtil.parseDates(startTime, endTime);
        Date startDate = dates[0];
        Date endDate = dates[1];

        Map<String, String> filterMap = new HashMap<>();
        GenericFilterMapBuilder.addToFilterMap(text, filterMap, "text", "");
        GenericFilterMapBuilder.addToFilterMap(startDate, filterMap, "timestamp", "gte");
        GenericFilterMapBuilder.addToFilterMap(endDate, filterMap, "timestamp", "lte");
        var notifications = notificationBean.getNotifications(filterMap, page, pageSize);
        var dtos = NotificationAssembler.from(notifications);
        long totalItems = notificationBean.getNotificationsCount(filterMap);
        long totalPages = (totalItems + pageSize - 1) / pageSize;
        PaginationMetadata paginationMetadata = new PaginationMetadata(page, pageSize, totalItems, totalPages, dtos.size());
        PaginationResponse<NotificationDTO> paginationResponse = new PaginationResponse<>(dtos, paginationMetadata);
        return Response.ok(paginationResponse).build();
    }

    @POST
    @Path("/")
    @RolesAllowed({"LogisticsOperator"})
    public Response create(ManufacturerDTO manufacturerDTO)
            throws MyEntityExistsException, MyEntityNotFoundException, MyConstraintViolationException {
        manufacturerBean.create(
                manufacturerDTO.getUsername(),
                manufacturerDTO.getPassword(),
                manufacturerDTO.getName(),
                manufacturerDTO.getEmail()
        );
        var manufacturer = manufacturerBean.find(manufacturerDTO.getUsername());
        return Response.status(Response.Status.CREATED).entity(ManufacturerAssembler.from(manufacturer)).build();
    }

    @PUT
    @Path("{username}")
    @Authenticated
    @RolesAllowed({"Manufacturer"})
    public Response update(@PathParam("username") String username, ManufacturerDTO manufacturerDTO)
            throws MyEntityNotFoundException {
        var principal = securityContext.getUserPrincipal();
        if (!principal.getName().equals(username)){
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        manufacturerBean.update(
                username,
                manufacturerDTO.getName(),
                manufacturerDTO.getEmail()
        );
        var manufacturer = manufacturerBean.find(username);
        return Response.ok(ManufacturerAssembler.from(manufacturer)).build();
    }

    @DELETE
    @Path("{username}")
    @Authenticated
    @RolesAllowed({"Manufacturer", "LogisticsOperator"})
    public Response delete(@PathParam("username") String username) throws MyEntityNotFoundException{
        var principal = securityContext.getUserPrincipal();
        if (!principal.getName().equals(username)){
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        Manufacturer manufacturer = manufacturerBean.delete(username);
        return Response.status(Response.Status.OK).entity(ManufacturerAssembler.from(manufacturer)).build();
    }

    @POST
    @Path("/{username}/email/send")
    @Authenticated
    @RolesAllowed({"LogisticsOperator"})
    public Response sendEmail(@PathParam("username") String username, EmailDTO email)
            throws MyEntityNotFoundException, MessagingException {
        Manufacturer manufacturer = manufacturerBean.find(username);
        if (manufacturer == null) {
            throw new MyEntityNotFoundException("Manufacturer with username '" + username
                    + "' not found in our records.");
        }
        emailBean.send(manufacturer.getEmail(), email.getSubject(), email.getMessage());
        return Response.status(Response.Status.OK).entity("E-mail sent").build();
    }
}
