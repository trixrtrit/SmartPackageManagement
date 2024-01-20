package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.ejbs;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.dtos.ProductDTO;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.dtos.TopCustomerDTO;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.dtos.TopProductDTO;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.Customer;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.Product;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Stateless
public class StatisticsBean {
    @PersistenceContext
    private EntityManager entityManager;

    @EJB
    private OrderBean orderBean;

    @EJB
    private ProductBean productBean;

    @EJB
    private QueryBean<Customer> customerQueryBean;

    @EJB
    private QueryBean<Product> productQueryBean;

    //Logistic Operator
    public long getTotalCustomers() {
        Map<String, String> emptyFilterMap = Map.of();
        return customerQueryBean.getEntitiesCount(Customer.class, emptyFilterMap);
    }
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public long getTotalOrders() {
        Map<String, String> filterMap = null;
        return orderBean.getOrdersCount(filterMap != null ? filterMap : Map.of());
    }

    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public long getTotalProducts() {
        Map<String, String> filterMap = null;
        return productBean.getProductsCount(filterMap != null ? filterMap : Map.of());
    }

    public List<TopCustomerDTO> getTopCustomers() {
        String queryString = "SELECT NEW pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.dtos.TopCustomerDTO(u.username, COUNT(o), SUM(o.totalPrice)) " +
                "FROM Customer u " +
                "JOIN u.orders o " +
                "GROUP BY u.username " +
                "ORDER BY COUNT(o) DESC";

        Query query = entityManager.createQuery(queryString, TopCustomerDTO.class);

        return query.getResultList();
    }

    public List<TopProductDTO> getTopProducts() {
        String queryString = "SELECT NEW pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.dtos.TopProductDTO(p.productReference, p.name, COUNT(oi)) " +
                "FROM Product p " +
                "JOIN p.orderItems oi " +
                "GROUP BY p.productReference, p.name " +
                "ORDER BY COUNT(oi) DESC";

        Query query = entityManager.createQuery(queryString, TopProductDTO.class);

        return query.getResultList();
    }

    //Customer
    public long getTotalOrdersByUsername(String username) {

        Map<String, String> filterMap = Map.of("username", username);
        return orderBean.getOrdersCount(filterMap);
    }

    public List<TopProductDTO> getTopProductsForCustomer(String username) {
        String queryString = "SELECT NEW pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.dtos.TopProductDTO(p.productReference, p.name,SUM (oi.quantity)) " +
                "FROM Product p " +
                "JOIN p.orderItems oi " +
                "JOIN oi.order o " +
                "JOIN o.customer c " +
                "WHERE c.username = :username " +
                "GROUP BY p.productReference, p.name " +
                "ORDER BY SUM (oi.quantity) DESC";

        Query query = entityManager.createQuery(queryString, TopProductDTO.class);
        query.setParameter("username", username);

        return query.getResultList();
    }

    public Map<String, Long> getManufacturerProductTotals(String username) {
        Map<String, Long> productTotals = new HashMap<>();

        // Total Products
        Long totalProducts = entityManager.createQuery("SELECT COUNT(p) FROM Product p WHERE p.manufacturer.username = :username", Long.class)
                .setParameter("username", username)
                .getSingleResult();
        productTotals.put("totalProducts", totalProducts);

        // Total Active Products
        Long totalActiveProducts = entityManager.createQuery("SELECT COUNT(p) FROM Product p WHERE p.manufacturer.username = :username AND p.isActive = true", Long.class)
                .setParameter("username", username)
                .getSingleResult();
        productTotals.put("totalActiveProducts", totalActiveProducts);

        // Total Inactive Products
        Long totalInactiveProducts = entityManager.createQuery("SELECT COUNT(p) FROM Product p WHERE p.manufacturer.username = :username AND p.isActive = false", Long.class)
                .setParameter("username", username)
                .getSingleResult();
        productTotals.put("totalInactiveProducts", totalInactiveProducts);

        return productTotals;
    }

    public List<TopProductDTO> getManufacturerTopProducts(String username) {
        String queryString = "SELECT NEW pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.dtos.TopProductDTO(p.productReference, p.name, COUNT(oi)) " +
                "FROM Product p " +
                "JOIN p.orderItems oi " +
                "WHERE p.manufacturer.username = :username " +
                "GROUP BY p.productReference, p.name " +
                "ORDER BY COUNT(oi) DESC";

        Query query = entityManager.createQuery(queryString, TopProductDTO.class);
        query.setParameter("username", username);

        return query.getResultList();
    }

    public List<TopProductDTO> getManufacturerOutOfStockWithPendingOrders(String username) {
        String queryString = "SELECT NEW pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.dtos.TopProductDTO(p.productReference, p.name, SUM(oi.quantity)) " +
                "FROM Product p " +
                "JOIN p.orderItems oi " +
                "JOIN oi.order o " +
                "WHERE p.manufacturer.username = :username AND o.status = 'PENDING' AND p.unitStock = 0 AND p.boxStock = 0 AND p.containerStock = 0 " +
                "GROUP BY p.productReference, p.name " +
                "ORDER BY SUM(oi.quantity) DESC";


        Query query = entityManager.createQuery(queryString, ProductDTO.class);
        query.setParameter("username", username);

        return query.getResultList();
    }

}
