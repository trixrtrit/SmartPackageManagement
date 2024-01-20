package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.ws;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.assemblers.OrderAssembler;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.dtos.OrderDTO;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.dtos.ProductDTO;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.dtos.TopCustomerDTO;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.dtos.TopProductDTO;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.ejbs.OrderBean;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.ejbs.StatisticsBean;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.enums.OrderStatus;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.pagination.PaginationMetadata;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.pagination.PaginationResponse;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.security.Authenticated;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.specifications.GenericFilterMapBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("statistics")
@Produces(MediaType.APPLICATION_JSON)
public class StatisticsService {

    @EJB
    private StatisticsBean statisticsBean;
    @EJB
    private OrderBean orderBean;
    @GET
    @Path("/totals")
    @Authenticated
    @RolesAllowed({"LogisticsOperator"})
    public Response getTotals() {
        TotalsResponse totalsResponse = new TotalsResponse(
                statisticsBean.getTotalCustomers(),
                statisticsBean.getTotalOrders(),
                statisticsBean.getTotalProducts()
        );
        return Response.ok(totalsResponse).build();
    }

    @GET
    @Path("/topCustomers")
    @Authenticated
    @RolesAllowed({"LogisticsOperator"})
    public Response getTopCustomers() {
        List<TopCustomerDTO> topCustomers = statisticsBean.getTopCustomers();
        return Response.ok(topCustomers).build();
    }

    @GET
    @Path("/topProducts")
    @Authenticated
    @RolesAllowed({"LogisticsOperator"})
    public Response getTopProducts() {
        List<TopProductDTO> topProducts = statisticsBean.getTopProducts();
        return Response.ok(topProducts).build();
    }

    @GET
    @Path("{username}/orders")
    @Authenticated
    @RolesAllowed({"Customer"})
    public Response getCustomerOrders(
            @PathParam("username") String username,
            @QueryParam("status") OrderStatus orderStatus,
            @DefaultValue("1") @QueryParam("page") int page,
            @DefaultValue("10") @QueryParam("pageSize") int pageSize) {
        Map<String, String> filterMap = new HashMap<>();
        GenericFilterMapBuilder.addToFilterMap(orderStatus, filterMap, "status", "equal");
        filterMap.put("Join/_/customer/_/username/_/equal", username);
        var orders = orderBean.getOrders(filterMap, page, pageSize);
        var dtos = OrderAssembler.fromNoOrderItems(orders);
        long totalItems = orderBean.getOrdersCount(filterMap);
        long totalPages = (totalItems + pageSize - 1) / pageSize;
        PaginationMetadata paginationMetadata = new PaginationMetadata(page, pageSize, totalItems, totalPages, dtos.size());
        PaginationResponse<OrderDTO> paginationResponse = new PaginationResponse<>(dtos, paginationMetadata);
        return Response.ok(paginationResponse).build();
    }

    private static class TotalsResponse {
        private final long totalCustomers;
        private final long totalOrders;
        private final long totalProducts;

        public TotalsResponse(long totalCustomers, long totalOrders, long totalProducts) {
            this.totalCustomers = totalCustomers;
            this.totalOrders = totalOrders;
            this.totalProducts = totalProducts;
        }

        public long getTotalCustomers() {
            return totalCustomers;
        }

        public long getTotalOrders() {
            return totalOrders;
        }

        public long getTotalProducts() {
            return totalProducts;
        }
    }

    private static class UserOrdersResponse {
        private final String username;
        private final long totalOrders;


        public UserOrdersResponse(String username, long totalOrders) {
            this.username = username;
            this.totalOrders = totalOrders;

        }

        public String getUsername() {
            return username;
        }

        public long getTotalOrders() {
            return totalOrders;
        }


    }

    @GET
    @Path("{username}/topProducts")
    @Authenticated
    @RolesAllowed({"Customer", "LogisticsOperator"})
    public Response getTopProductsForCustomer(@PathParam("username") String username) {
        List<TopProductDTO> topProducts = statisticsBean.getTopProductsForCustomer(username);
        return Response.ok(topProducts).build();
    }
//manufacturers
@GET
@Path("{username}/productTotals")
@Authenticated
@RolesAllowed({"Manufacturer"})
public Response getManufacturerProductTotals(@PathParam("username") String username) {
    Map<String, Long> productTotals = statisticsBean.getManufacturerProductTotals(username);
    return Response.ok(productTotals).build();
}
    @GET
    @Path("{username}/topProductsManufacturer")
    @Authenticated
    @RolesAllowed({"Manufacturer"})
    public Response getManufacturerTopProducts(@PathParam("username") String username) {
        List<TopProductDTO> topProducts = statisticsBean.getManufacturerTopProducts(username);
        return Response.ok(topProducts).build();
    }
    @GET
    @Path("{username}/outOfStockWithPendingOrders")
    @Authenticated
    @RolesAllowed({"Manufacturer"})
    public Response getManufacturerOutOfStockWithPendingOrders(@PathParam("username") String username) {
        List<TopProductDTO> outOfStockWithPendingOrders = statisticsBean.getManufacturerOutOfStockWithPendingOrders(username);
        return Response.ok(outOfStockWithPendingOrders).build();
    }


}
