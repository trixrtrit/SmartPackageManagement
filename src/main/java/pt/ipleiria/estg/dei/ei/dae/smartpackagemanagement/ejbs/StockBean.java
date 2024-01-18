package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.ejbs;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.ConstraintViolationException;
import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.dtos.PackageDTO;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.Package;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.Product;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.Stock;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyValidationException;

import java.util.List;

@Stateless
public class StockBean {
    @PersistenceContext
    private EntityManager entityManager;

    @EJB
    private QueryBean<Package> packageQueryBean;

    public long create (Product product) throws MyConstraintViolationException {
        try {
            Stock stock = new Stock(product);
            entityManager.persist(stock);
            return stock.getId();
        } catch (ConstraintViolationException err) {
            throw new MyConstraintViolationException(err);
        }
    }

    public Stock find(long id) throws MyEntityNotFoundException {
        Stock stock = entityManager.find(Stock.class, id);
        if (stock == null) {
            throw new MyEntityNotFoundException("The stock with the id: " + id + " does not exist");
        }
        Hibernate.initialize(stock.getProduct());
        return stock;
    }

    //add 1a package a lista?
    public void addPrimaryPackage(long id, long packageCode) throws MyEntityNotFoundException, MyValidationException {
        Stock stock = entityManager.find(Stock.class, id);
        Package aPackage = entityManager.find(Package.class, packageCode);
        if (stock == null) {
            throw new MyEntityNotFoundException("The stock with the id: " + id + " does not exist");
        }
        if (aPackage == null) {
            throw new MyEntityNotFoundException("The package with the code: " + packageCode + " does not exist");
        }
        if (stock.getPrimaryPackageList().contains(aPackage)) {
            throw new MyValidationException("Package already exists in the primary package list");
        }
        stock.addPrimaryPackage(aPackage);
    }

    //add 2a package a lista?
    public void addSecondaryPackage(long id, long packageCode) throws MyEntityNotFoundException, MyValidationException {
        Stock stock = entityManager.find(Stock.class, id);
        Package aPackage = entityManager.find(Package.class, packageCode);
        if (stock == null) {
            throw new MyEntityNotFoundException("The stock with the id: " + id + " does not exist");
        }
        if (aPackage == null) {
            throw new MyEntityNotFoundException("The package with the code: " + packageCode + " does not exist");
        }
        if (stock.getSecondaryPackageList().contains(aPackage)) {
            throw new MyValidationException("Package already exists in the secondary package list");
        }
        stock.addSecondaryPackage(aPackage);
    }

    //remover 1a package a lista?
    public void removePrimaryPackage(long id, long packageCode) throws MyEntityNotFoundException, MyValidationException {
        Stock stock = entityManager.find(Stock.class, id);
        Package aPackage = entityManager.find(Package.class, packageCode);
        if (stock == null) {
            throw new MyEntityNotFoundException("The stock with the id: " + id + " does not exist");
        }
        if (aPackage == null) {
            throw new MyEntityNotFoundException("The package with the code: " + packageCode + " does not exist");
        }
        if (!stock.getPrimaryPackageList().contains(aPackage)) {
            throw new MyValidationException("Package does not exist in the primary package list");
        }
        stock.removePrimaryPackage(aPackage);
    }

    //remover 2a package a lista?
    public void removeSecondaryPackage(long id, long packageCode) throws MyEntityNotFoundException, MyValidationException {
        Stock stock = entityManager.find(Stock.class, id);
        Package aPackage = entityManager.find(Package.class, packageCode);
        if (stock == null) {
            throw new MyEntityNotFoundException("The stock with the id: " + id + " does not exist");
        }
        if (aPackage == null) {
            throw new MyEntityNotFoundException("The package with the code: " + packageCode + " does not exist");
        }
        if (!stock.getSecondaryPackageList().contains(aPackage)) {
            throw new MyValidationException("Package does not exist in the secondary package list");
        }
        stock.removeSecondaryPackage(aPackage);
    }

    //add lista de 1packages a lista
    public void addPrimaryPackageList(long id, List<Long> packageCodeList) throws MyEntityNotFoundException, MyValidationException {
        Stock stock = entityManager.find(Stock.class, id);
        if (stock == null) {
            throw new MyEntityNotFoundException("The stock with the id: " + id + " does not exist");
        }

        for (Long packageCode : packageCodeList) {
            Package aPackage = entityManager.find(Package.class, packageCode);
            if (aPackage == null) {
                continue;
                //throw new MyEntityNotFoundException("The package with the code: " + packageCode + " does not exist");
            }
            if (stock.getPrimaryPackageList().contains(aPackage)) {
                continue;
                //throw new MyValidationException("Package " + aPackage.getCode() + " already exists in the primary package list");
            }
            stock.addPrimaryPackage(aPackage);
        }
    }

    //add lista de 2packages a lista
    public void addSecondaryPackageList(long id, List<Long> packageCodeList) throws MyEntityNotFoundException, MyValidationException {
        Stock stock = entityManager.find(Stock.class, id);
        if (stock == null) {
            throw new MyEntityNotFoundException("The stock with the id: " + id + " does not exist");
        }

        for (Long packageCode : packageCodeList) {
            Package aPackage = entityManager.find(Package.class, packageCode);
            if (aPackage == null) {
                continue;
                //throw new MyEntityNotFoundException("The package with the code: " + packageCode + " does not exist");
            }
            if (stock.getSecondaryPackageList().contains(aPackage)) {
                continue;
                //throw new MyValidationException("Package " + aPackage.getCode() + " already exists in the secondary package list");
            }
            stock.addSecondaryPackage(aPackage);
        }
    }

    //remover lista de 1packages a lista
    public void removePrimaryPackageList(long id, List<Long> packageCodeList) throws MyEntityNotFoundException, MyValidationException {
        Stock stock = entityManager.find(Stock.class, id);
        if (stock == null) {
            throw new MyEntityNotFoundException("The stock with the id: " + id + " does not exist");
        }

        for (Long packageCode : packageCodeList) {
            Package aPackage = entityManager.find(Package.class, packageCode);
            if (aPackage == null) {
                continue;
                //throw new MyEntityNotFoundException("The package with the code: " + packageCode + " does not exist");
            }
            if (!stock.getPrimaryPackageList().contains(aPackage)) {
                continue;
                //throw new MyValidationException("Package " + aPackage.getCode() + " does not exist in the primary package list");
            }
            stock.removePrimaryPackage(aPackage);
        }
    }

    //remover lista de 2packages a lista
    public void removeSecondaryPackageList(long id, List<Long> packageCodeList) throws MyEntityNotFoundException, MyValidationException {
        Stock stock = entityManager.find(Stock.class, id);
        if (stock == null) {
            throw new MyEntityNotFoundException("The stock with the id: " + id + " does not exist");
        }

        for (Long packageCode : packageCodeList) {
            Package aPackage = entityManager.find(Package.class, packageCode);
            if (aPackage == null) {
                continue;
                //throw new MyEntityNotFoundException("The package with the code: " + packageCode + " does not exist");
            }
            if (!stock.getSecondaryPackageList().contains(aPackage)) {
                continue;
                //throw new MyValidationException("Package " + aPackage.getCode() + " does not exist in the secondary package list");
            }
            stock.removeSecondaryPackage(aPackage);
        }
    }

    public List<Package> getPrimaryPackageList(long id) throws MyEntityNotFoundException {
        Stock stock = entityManager.find(Stock.class, id);
        if (stock == null) {
            throw new MyEntityNotFoundException("The stock with the id: " + id + " does not exist");
        }
        return stock.getPrimaryPackageList();
    }
    //get lista de 2packages
    public List<Package> getSecondaryPackageList(long id) throws MyEntityNotFoundException {
        Stock stock = entityManager.find(Stock.class, id);
        if (stock == null) {
            throw new MyEntityNotFoundException("The stock with the id: " + id + " does not exist");
        }
        return stock.getSecondaryPackageList();
    }
    public int getPrimaryPackageListCount(long id) throws MyEntityNotFoundException {
        Stock stock = entityManager.find(Stock.class, id);
        if (stock == null) {
            throw new MyEntityNotFoundException("The stock with the id: " + id + " does not exist");
        }
        return stock.getPrimaryPackageList().size();
    }

    public int getSecondaryPackageListCount(long id) throws MyEntityNotFoundException {
        Stock stock = entityManager.find(Stock.class, id);
        if (stock == null) {
            throw new MyEntityNotFoundException("The stock with the id: " + id + " does not exist");
        }
        return stock.getSecondaryPackageList().size();
    }
}
