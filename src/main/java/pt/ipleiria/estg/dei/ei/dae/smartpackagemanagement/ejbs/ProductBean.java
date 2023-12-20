package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.ConstraintViolationException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.Manufacturer;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.Package;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.Product;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyEntityNotFoundException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Stateless
public class ProductBean {
    @PersistenceContext
    private EntityManager entityManager;

    public long create(
            String name,
            String description,
            double price,
            String manufacturer_username,
            String reference
    ) throws MyEntityNotFoundException, MyConstraintViolationException {
        Manufacturer manufacturer = entityManager.find(Manufacturer.class, manufacturer_username);
        if (manufacturer == null){
            throw new MyEntityNotFoundException("Manufacturer with username '" + manufacturer_username + "' not found.");
        }
        //Package aPackage = new Package("a", "b", manufacturer, new LogisticsOperator("a" + package_id, "a", "b", "c@gmail.com"));
/*
        Package aPackage = entityManager.find(Package.class, package_id);
        if (aPackage == null){
            throw new MyEntityNotFoundException("Package with id '" + package_id + "' not found.");
        }*/

        try {
            //Product product = new Product(name, description, price, manufacturer, aPackage, true, stock);
            Product product = new Product(name, description, price, manufacturer, true, reference);
            entityManager.persist(product);
            return product.getId();
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
    //TODO: export
    public void export() throws IOException {
        Map<Integer, String> productAttributesMap = new HashMap<>();

        productAttributesMap.put(0, "Id");
        productAttributesMap.put(1, "Name");
        productAttributesMap.put(2, "Description");
        productAttributesMap.put(3, "Price");
        productAttributesMap.put(4, "Manufacturer");
        productAttributesMap.put(5, "IsActive");
        productAttributesMap.put(6, "Reference");

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Products");
        Row header = sheet.createRow(0);

        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        XSSFFont font = ((XSSFWorkbook) workbook).createFont();
        font.setFontName("Arial");
        font.setFontHeightInPoints((short) 16);
        font.setBold(true);
        headerStyle.setFont(font);

        Cell headerCell;
        for (int i = 0; i < productAttributesMap.keySet().size(); i++){
            headerCell = header.createCell(i);
            headerCell.setCellValue(productAttributesMap.get(i));
            headerCell.setCellStyle(headerStyle);
        }

        CellStyle style = workbook.createCellStyle();
        style.setWrapText(true);

        int rowNum = 1;
        List<Product> productList = getProducts();
        for (Product product : productList) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(product.getId());
            row.createCell(1).setCellValue(product.getName());
            row.createCell(2).setCellValue(product.getDescription());
            row.createCell(3).setCellValue(product.getPrice());
            row.createCell(4).setCellValue(product.getManufacturer().getName());
            row.createCell(5).setCellValue(product.isActive());
            row.createCell(6).setCellValue(product.getProductReference());
        }

        String resourceDirectory = getClass().getClassLoader().getResource("").getPath();
        String fileLocation = resourceDirectory + "temp.xlsx";
        System.out.println(fileLocation);

        FileOutputStream outputStream = new FileOutputStream(fileLocation);
        workbook.write(outputStream);
        workbook.close();
    }

    public void update(
            long id,
            String name,
            String description,
            double price,
            String reference
    ) throws MyEntityNotFoundException, MyConstraintViolationException {
        Product product = this.find(id);
        entityManager.lock(product, LockModeType.OPTIMISTIC);
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setProductReference(reference);
    }
    public void changeActiveStatus(long id) throws MyEntityNotFoundException{
        Product product = this.find(id);
        entityManager.lock(product, LockModeType.OPTIMISTIC);
        product.setActive(!product.isActive());
    }

    public void setStocks(long id, float unitStock, float boxStock, float containerStock)
            throws MyEntityNotFoundException{
        Product product = this.find(id);
        entityManager.lock(product, LockModeType.OPTIMISTIC);
        product.setUnitStock(unitStock);
        product.setBoxStock(boxStock);
        product.setContainerStock(containerStock);
    }

    public void setPackage(long id, long packageId) throws MyEntityNotFoundException {
        Product product = this.find(id);
        Package aPackage = entityManager.find(Package.class, packageId);
        if (aPackage == null){
            throw new MyEntityNotFoundException("Package with id '" + packageId + "' not found.");
        }
        entityManager.lock(product, LockModeType.OPTIMISTIC);
        product.setaPackage(aPackage);
    }

    public Product delete(long id) throws MyEntityNotFoundException {
        Product product = this.find(id);
        entityManager.remove(product);
        return product;
    }

}
