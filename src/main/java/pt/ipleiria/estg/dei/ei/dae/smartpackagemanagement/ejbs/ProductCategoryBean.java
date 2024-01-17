package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.validation.ConstraintViolationException;
import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.ProductCategory;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyIllegalConstraintException;

import java.util.List;

@Stateless
public class ProductCategoryBean {
    @PersistenceContext
    private EntityManager entityManager;

    public long create(String category)
            throws MyEntityExistsException, MyConstraintViolationException, MyEntityNotFoundException {

        if (exists(category)) {
            throw new MyEntityExistsException("Product category '" + category + " already exists.");
        }
        try {
            var productCategory = new ProductCategory(category);
            entityManager.persist(productCategory);
            return productCategory.getId();
        } catch (ConstraintViolationException err) {
            throw new MyConstraintViolationException(err);
        }
    }
    public boolean exists(String category) {
        var query = entityManager.createQuery(
                "SELECT COUNT(*) FROM ProductCategory st WHERE st.category = :category", Long.class
        );
        query.setParameter("category", category);
        return (Long) query.getSingleResult() > 0L;
    }

    public List<ProductCategory> getCategories(){
        Query query = entityManager.createNamedQuery("getProductCategories", ProductCategory.class);
        var result = query.getResultList();

        return result;
    }

    public ProductCategory find(long id) throws MyEntityNotFoundException {
        var productCategory = entityManager.find(ProductCategory.class, id);
        if (productCategory == null) {
            throw new MyEntityNotFoundException("Product category with id: '" + id + "' doest not exist.");
        }
        return productCategory;
    }

    public void update(long id, String category) throws MyEntityNotFoundException, MyConstraintViolationException {
        var productCategory = this.find(id);
        entityManager.lock(productCategory, LockModeType.OPTIMISTIC);
        productCategory.setCategory(category);
    }

    public ProductCategory delete(long id) throws MyEntityNotFoundException, MyIllegalConstraintException {
        var productCategory = this.find(id);

        Hibernate.initialize(productCategory.getProducts());

        if (productCategory.getProducts().stream().count() > 0){
            throw new MyIllegalConstraintException("Cannot delete product category that still has products attached.");
        }

        entityManager.remove(productCategory);
        return productCategory;
    }
}
