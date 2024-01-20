package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.ejbs;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.persistence.*;
import jakarta.persistence.criteria.*;
import jakarta.validation.ConstraintViolationException;
import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.*;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.enums.PackageType;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyPackageProductAssociationViolationException;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Stateless
public class StandardPackageBean {
    @PersistenceContext
    private EntityManager entityManager;
    @EJB
    private PackageBean packageBean;
    @EJB
    private ProductBean productBean;
    @EJB
    private QueryBean<StandardPackage> standardPackageQueryBean;

    public long create(long code, String material, PackageType packageType, Date manufactureDate, Long initialProductId)
            throws MyEntityNotFoundException, MyConstraintViolationException, MyEntityExistsException {
        if (exists(code)) {
            throw new MyEntityExistsException("A package with the code: " + code + " already exists");
        }
        try {
            StandardPackage standardPackage = new StandardPackage(code, material, packageType, manufactureDate);

            if (initialProductId != null){//packageType != PackageType.TERTIARY &&
                var product = entityManager.find(Product.class, initialProductId);
                if (product == null){
                    throw new MyEntityNotFoundException("Product with id '" + initialProductId + "' for the package does not exist");
                }
                //standardPackage.addProduct(product);
                entityManager.persist(standardPackage);
                addProductToPackage(code, initialProductId);//
                standardPackage.setInitialProductId(initialProductId);
                productBean.addUnitStock(initialProductId);
            }
            //entityManager.persist(standardPackage);
            return standardPackage.getCode();
        } catch (ConstraintViolationException err) {
            throw new MyConstraintViolationException(err);
        } catch (MyPackageProductAssociationViolationException e) {
            throw new RuntimeException(e);
        }
    }

    public List<StandardPackage> getStandardPackages(Map<String, String> filterMap, int pageNumber, int pageSize)
            throws IllegalArgumentException {
        Map<String, String> orderMap = new LinkedHashMap<>();
        orderMap.put("code", "asc");
        orderMap.put("packageType", "asc");
        return standardPackageQueryBean.getEntities(StandardPackage.class, filterMap, orderMap, pageNumber, pageSize);
    }

    public long getStandardPackagesCount(Map<String, String> filterMap) {
        return standardPackageQueryBean.getEntitiesCount(StandardPackage.class, filterMap);
    }

    public StandardPackage find(long code) throws MyEntityNotFoundException {
        StandardPackage standardPackage = entityManager.find(StandardPackage.class, code);
        if (standardPackage == null) {
            throw new MyEntityNotFoundException("The package with the code: " + code + " does not exist");
        }
        Hibernate.initialize(standardPackage.getSensorPackageList());
        Hibernate.initialize(standardPackage.getTransportPackageStandardPackages());
        return standardPackage;
    }

    public StandardPackage getStandardPackageProducts(long code) throws MyEntityNotFoundException {
        if(!exists(code)) {
            throw new MyEntityNotFoundException("The package with the code: " + code + " does not exist");
        }
        StandardPackage standardPackage = entityManager.find(StandardPackage.class, code);
        Hibernate.initialize(standardPackage.getStandardPackageProducts());
        return standardPackage;
    }

    public List<StandardPackage> getForDelivery(Long productId, PackageType packageType, int page, int pageSize){
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<StandardPackage> query = builder.createQuery(StandardPackage.class);
        Root<StandardPackage> root = query.from(StandardPackage.class);

        // Join with StandardPackageProduct
        Join<StandardPackage, StandardPackageProduct> standardPackageProductJoin = root.join("standardPackageProducts", JoinType.INNER);

        // Join with Product
        Join<StandardPackageProduct, Product> productJoin = standardPackageProductJoin.join("product", JoinType.INNER);

        Predicate combinedPredicate = getForDeliveryCombinedPredicate(productId, packageType, builder, root, productJoin);

        // Set the query conditions
        query.select(root).where(combinedPredicate);

        // Pagination
        TypedQuery<StandardPackage> typedQuery = entityManager.createQuery(query);
        typedQuery.setFirstResult((page - 1) * pageSize);
        typedQuery.setMaxResults(pageSize);

        // Execute the query
        return typedQuery.getResultList();
    }

    public long getForDeliveryCount(Long productId, PackageType packageType) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = builder.createQuery(Long.class);
        Root<StandardPackage> root = query.from(StandardPackage.class);
        // Join with StandardPackageProduct
        Join<StandardPackage, StandardPackageProduct> standardPackageProductJoin = root.join("standardPackageProducts", JoinType.INNER);

        // Join with Product
        Join<StandardPackageProduct, Product> productJoin = standardPackageProductJoin.join("product", JoinType.INNER);

        Predicate combinedPredicate = getForDeliveryCombinedPredicate(productId, packageType, builder, root, productJoin);

        query.select(builder.count(root));
        query.where(builder.and(combinedPredicate));

        return entityManager.createQuery(query).getSingleResult();
    }

    private static Predicate getForDeliveryCombinedPredicate(Long productId, PackageType packageType, CriteriaBuilder builder, Root<StandardPackage> root, Join<StandardPackageProduct, Product> productJoin) {
        // Conditions
        Predicate packageTypePredicate = builder.equal(root.get("packageType"), packageType);
        Predicate productPredicate = builder.equal(productJoin.get("id"), productId);
        Predicate activePackagePredicate = builder.isTrue(root.get("isActive"));  // Assuming there's an 'isActive' field

        // Combine conditions
        Predicate combinedPredicate = builder.and(packageTypePredicate, productPredicate, activePackagePredicate);
        return combinedPredicate;
    }

    public StandardPackage getPackageSensors(long code) throws MyEntityNotFoundException {
        if(!this.exists(code)) {
            throw new MyEntityNotFoundException("The package with the code: " + code + " does not exist");
        }
        StandardPackage standardPackage = entityManager.find(StandardPackage.class, code);
        Hibernate.initialize(standardPackage.getSensorPackageList());
        return standardPackage;
    }

    public boolean exists(long code) {
        Query query = entityManager.createNamedQuery("standardPackageExists", StandardPackage.class);
        query.setParameter("code", code);
        return (Long) query.getSingleResult() > 0L;
    }

    public StandardPackage update(long code, String material, PackageType packageType)
            throws MyEntityNotFoundException, MyConstraintViolationException {
        StandardPackage standardPackage = this.find(code);
        entityManager.lock(standardPackage, LockModeType.OPTIMISTIC);
        standardPackage.setMaterial(material);
        standardPackage.setPackageType(packageType);
        return standardPackage;
    }

    public StandardPackageProduct findStandardPackageProduct(long standardPkgCode, long productId)
            throws MyEntityNotFoundException{
        try {
            return entityManager.createNamedQuery("findStandardPackageProduct", StandardPackageProduct.class)
                    .setParameter("standardPkgCode", standardPkgCode)
                    .setParameter("productId", productId)
                    .getSingleResult();
        } catch (NoResultException e) {
            throw new MyEntityNotFoundException("Product with id: " + productId +
                    " is not associated to the package code: " + standardPkgCode);
        }
    }

    private long standardPackageAssociatedToProductCount(long code, long productId){
        Query query = entityManager.createNamedQuery("standardPackageProductExists", StandardPackageProduct.class)
                .setParameter("standardPkgCode", code)
                .setParameter("productId", productId);
        return (Long) query.getSingleResult();
    }

    public void addProductToPackage(long code, long productId)
            throws MyEntityNotFoundException, MyPackageProductAssociationViolationException {
        StandardPackage standardPackage = find(code);
        if (standardPackage == null)
            throw new MyEntityNotFoundException("The package with the code: " + code + " does not exist");

        Product product = entityManager.find(Product.class, productId);
        if (product == null)
            throw new MyEntityNotFoundException("The product with the id: " + productId + " does not exist");
        if (standardPackage.getPackageType() != PackageType.TERTIARY && standardPackageAssociatedToProductCount(code, productId) > 0) {
            throw new MyPackageProductAssociationViolationException("Non-tertiary packages cannot have more than one product");
        }
        StandardPackageProduct standardPackageProduct = new StandardPackageProduct(standardPackage, product);
        standardPackage.getStandardPackageProducts().add(standardPackageProduct);
        product.getStandardPackageProducts().add(standardPackageProduct);
        entityManager.persist(standardPackageProduct);
    }

    public Product findCurrentProductByPackageId(Long packageId) {
        TypedQuery<Product> query = entityManager.createNamedQuery("findCurrentProductByPackageId", Product.class);
        query.setParameter("packageId", packageId);
        return query.getSingleResult();
    }

    public void removeProductFromPackage(long code, long productId) throws MyEntityNotFoundException {
        StandardPackage standardPackage = find(code);
        if (standardPackage == null)
            throw new MyEntityNotFoundException("The package with the code: " + code + " does not exist");

        Product product = entityManager.find(Product.class, productId);
        if (product == null)
            throw new MyEntityNotFoundException("The product with the id: " + productId + " does not exist");
        StandardPackageProduct standardPackageProduct = findStandardPackageProduct(code, productId);
        standardPackageProduct.setRemovedAt(new Date());
        standardPackage.getStandardPackageProducts().remove(standardPackageProduct);
        product.getStandardPackageProducts().remove(standardPackageProduct);
    }

    public void addSensorToPackage(long code, long sensorId) throws MyEntityNotFoundException, MyEntityExistsException {
        packageBean.addSensorToPackage(code, sensorId, StandardPackage.class);
    }

    public void removeSensorFromPackage(long code, long sensorId) throws MyEntityNotFoundException {
        packageBean.removeSensorFromPackage(code, sensorId, StandardPackage.class);
    }

}
