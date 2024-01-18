package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.ejbs;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.ConstraintViolationException;
import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.Package;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.Product;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.StandardPackage;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.enums.PackageType;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyPackageProductAssociationViolationException;

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
    private QueryBean<StandardPackage> standardPackageQueryBean;

    public long create(long code, String material, PackageType packageType)
            throws MyEntityNotFoundException, MyConstraintViolationException, MyEntityExistsException {
        if (packageBean.exists(code)) {
            throw new MyEntityExistsException("A package with the code: " + code + " already exists");
        }
        try {
            StandardPackage standardPackage = new StandardPackage(code, material, packageType);
            entityManager.persist(standardPackage);
            return standardPackage.getCode();
        } catch (ConstraintViolationException err) {
            throw new MyConstraintViolationException(err);
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
        Hibernate.initialize(standardPackage.getProducts());
        return standardPackage;
    }

    public StandardPackage getStandardPackageProducts(long code) throws MyEntityNotFoundException {
        if(!packageBean.exists(code)) {
            throw new MyEntityNotFoundException("The package with the code: " + code + " does not exist");
        }
        StandardPackage standardPackage = entityManager.find(StandardPackage.class, code);
        Hibernate.initialize(standardPackage.getProducts());
        return standardPackage;
    }

    public void addProductToPackage(long code, long productId)
            throws MyEntityNotFoundException, MyPackageProductAssociationViolationException {
        StandardPackage standardPackage = find(code);
        if (standardPackage == null)
            throw new MyEntityNotFoundException("The package with the code: " + code + " does not exist");

        Product product = entityManager.find(Product.class, productId);
        if (product == null)
            throw new MyEntityNotFoundException("The product with the id: " + productId + " does not exist");
        if (standardPackage.getPackageType() != PackageType.TERTIARY && !standardPackage.getProducts().isEmpty()) {
            throw new MyPackageProductAssociationViolationException("Non-tertiary packages cannot have more than one product");
        }
        standardPackage.addProduct(product);
        product.addStandardPackage(standardPackage);
    }

    public void removeProductFromPackage(long code, long productId) throws MyEntityNotFoundException {
        StandardPackage standardPackage = find(code);
        if (standardPackage == null)
            throw new MyEntityNotFoundException("The package with the code: " + code + " does not exist");

        Product product = entityManager.find(Product.class, productId);
        if (product == null)
            throw new MyEntityNotFoundException("The product with the id: " + productId + " does not exist");
        standardPackage.removeProduct(product);
        product.removePackage(standardPackage);
    }

    public void addSensorToPackage(long code, long sensorId) throws MyEntityNotFoundException, MyEntityExistsException {
        packageBean.addSensorToPackage(code, sensorId);
    }

    public void removeSensorFromPackage(long code, long sensorId) throws MyEntityNotFoundException {
        packageBean.removeSensorFromPackage(code, sensorId);
    }

}
