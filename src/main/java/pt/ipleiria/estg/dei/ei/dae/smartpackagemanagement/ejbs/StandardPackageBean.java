package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.ejbs;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.persistence.*;
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
        if(initialProductId == null){
            throw new MyEntityNotFoundException("Initial product id cannot be null");
        }
        if (exists(code)) {
            throw new MyEntityExistsException("A package with the code: " + code + " already exists");
        }
        try {
            StandardPackage standardPackage = new StandardPackage(code, material, packageType, manufactureDate);//, initialProductId);

            if (initialProductId != null){//packageType != PackageType.TERTIARY &&
                var product = entityManager.find(Product.class, initialProductId);
                if (product == null){
                    throw new MyEntityNotFoundException("Product with id '" + initialProductId + "' for the package does not exist");
                }
                //standardPackage.addProduct(product);
                entityManager.persist(standardPackage);
                addProductToPackage(code, initialProductId);//
                standardPackage.setInitialProductId(initialProductId);//
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

    public long createMany(long code, String material, PackageType packageType, Date manufactureDate, Long initialProductId, Long initialAmountCreation)
            throws MyEntityNotFoundException, MyConstraintViolationException, MyEntityExistsException {
        if(initialProductId == null){
            throw new MyEntityNotFoundException("Initial product id cannot be null");
        }
        try {
            long totalPackagesCreated = 0;
            boolean stop = false;
            for(int i = 0; i < initialAmountCreation; i++) {
                code = code + i;
                int attempts = 0;
                int maxAttempts = 10;
                while (exists(code)) {
                    code = code + 1;
                    attempts++;
                    if (attempts >= maxAttempts) {
                        stop = true;
                        //throw new MyEntityExistsException("A package with the code: " + code + " already exists after " + maxAttempts + " attempts to increment it. Please choose another code.");
                    }
                }
                if(stop == true) {
                    break;
                }
                StandardPackage standardPackage = new StandardPackage(code, material, packageType, manufactureDate);//, initialProductId);

                if (initialProductId != null) {//packageType != PackageType.TERTIARY &&
                    var product = entityManager.find(Product.class, initialProductId);
                    if (product == null) {
                        throw new MyEntityNotFoundException("Product with id '" + initialProductId + "' for the package does not exist");
                    }
                    entityManager.persist(standardPackage);
                    addProductToPackage(code, initialProductId);
                    standardPackage.setInitialProductId(initialProductId);//
                    productBean.addUnitStock(initialProductId);

                    totalPackagesCreated++;
                }
            }
            return totalPackagesCreated;
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
