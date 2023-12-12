package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.ConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.Manufacturer;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.Package;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.Product;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyEntityNotFoundException;

import java.util.List;

@Stateless
public class ProductBean {
    @PersistenceContext
    private EntityManager entityManager;

    public void create(
            String name,
            String description,
            double price,
            String manufacturer_username,
            long package_id,
            float stock
    ) throws MyEntityNotFoundException, MyConstraintViolationException {
        Manufacturer manufacturer = entityManager.find(Manufacturer.class, manufacturer_username);
        if (manufacturer == null){
            throw new MyEntityNotFoundException("Manufacturer with username '" + manufacturer_username + "' not found.");
        }

        Package aPackage = entityManager.find(Package.class, package_id);
        if (aPackage == null){
            throw new MyEntityNotFoundException("Package with id '" + package_id + "' not found.");
        }

        try {
            Product product = new Product(name, description, price, manufacturer, aPackage, true, stock);
            entityManager.persist(product);
        } catch (ConstraintViolationException err) {
            throw new MyConstraintViolationException(err);
        }
    }

    public List<Product> getProducts() {
        return entityManager.createNamedQuery("getProducts", Product.class).getResultList();
    }

    public Product find(long id) throws MyEntityNotFoundException {
        Product product = entityManager.find(Product.class, id);
        if (product == null) {
            throw new MyEntityNotFoundException("The product with the id: " + id + " does not exist");
        }
        return product;
    }

    //TODO: load function with excel

    //TODO: update manufacturer, update package, update isActive status. differnt endpoints?
    // qq tem de acontecer para alterar o estado do product para isActive false?
    public void update(
            long id,
            String name,
            String description,
            double price,
            float stock
    ) throws MyEntityNotFoundException {
        Product product = this.find(id);
        entityManager.lock(product, LockModeType.OPTIMISTIC);
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setStock(stock);
    }

    public Product delete(long id) throws MyEntityNotFoundException {
        Product product = this.find(id);
        entityManager.remove(product);
        return product;
    }
}
