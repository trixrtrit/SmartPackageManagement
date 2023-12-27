package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.ConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.*;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyEntityNotFoundException;

import java.util.List;

@Stateless
public class ProductParameterBean {
    @PersistenceContext
    private EntityManager entityManager;

    public long create(Long productId, Long sensorTypeId, float minValue, float maxValue)
            throws MyEntityExistsException, MyConstraintViolationException, MyEntityNotFoundException {

        var product = (Product)entityManager.find(Product.class, productId);

        if (product == null){
            throw new MyEntityNotFoundException("Product with id '" + productId + "' not found.");
        }

        var sensorType = (SensorType)entityManager.find(SensorType.class, sensorTypeId);

        if (sensorType == null){
            throw new MyEntityNotFoundException("Sensor Type with id '" + sensorTypeId + "' not found.");
        }

        if (exists(productId, sensorTypeId)) {
            throw new MyEntityExistsException("Product Parameter for the Product #'" + productId + "' and Sensor Type #'" + sensorTypeId + "' already exists.");
        }
        try {
            var productParameter = new ProductParameter(product, sensorType, minValue, maxValue);
            entityManager.persist(productParameter);
            return productParameter.getId();
        } catch (ConstraintViolationException err) {
            throw new MyConstraintViolationException(err);
        }
    }

    public List<ProductParameter> getProductParameters() {
        return entityManager.createNamedQuery("getProductParameters", ProductParameter.class).getResultList();
    }

    public boolean exists(Long productId, Long sensorTypeId) {
        var query = entityManager.createQuery(
                "SELECT COUNT(*) FROM ProductParameter pp WHERE pp.product.id = :productId AND pp.sensorType.id = :sensorTypeId", Long.class
        );
        query.setParameter("productId", productId);
        query.setParameter("sensorTypeId", sensorTypeId);
        return query.getSingleResult() > 0L;
    }

    public ProductParameter find(Long id) throws MyEntityNotFoundException {
        var productParameter = (ProductParameter)entityManager.find(ProductParameter.class, id);
        if (productParameter == null) {
            throw new MyEntityNotFoundException("The Product parameter with the id: " + id + " does not exist");
        }
        return productParameter;
    }

    public void update(long id, float minValue, float maxValue) throws MyEntityNotFoundException, MyConstraintViolationException {
        var productParameter = this.find(id);
        entityManager.lock(productParameter, LockModeType.OPTIMISTIC);
        productParameter.setMinValue(minValue);
        productParameter.setMaxValue(maxValue);
    }

    public ProductParameter delete(long id) throws MyEntityNotFoundException {
        var productParameter = this.find(id);
        entityManager.remove(productParameter);
        return productParameter;
    }

}
