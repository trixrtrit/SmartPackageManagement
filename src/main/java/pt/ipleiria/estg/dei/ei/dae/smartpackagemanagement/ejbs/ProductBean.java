package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.validation.ConstraintViolationException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.Manufacturer;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.Package;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.Product;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.ProductParameter;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyEntityNotFoundException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
        try {
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

    public List<Product> getProductsForExport() {
        return entityManager.createNamedQuery("getProductsForExport", Product.class).getResultList();
    }

    public boolean exists(Long id) {
        Query query = entityManager.createNamedQuery("productExists", Product.class);
        query.setParameter("id", id);
        return (Long) query.getSingleResult() > 0L;
    }

    public Product find(long id) throws MyEntityNotFoundException {
        Product product = entityManager.find(Product.class, id);
        if (product == null) {
            throw new MyEntityNotFoundException("The product with the id: " + id + " does not exist");
        }
        return product;
    }

    public Product getProductPackages(long id) throws MyEntityNotFoundException {
        if(!this.exists(id)) {
            throw new MyEntityNotFoundException("The product with the id: " + id + " does not exist");
        }
        Product product = entityManager.find(Product.class, id);
        Hibernate.initialize(product.getPackages());
        return product;
    }

    public List<ProductParameter> getProductParametersWithSensorType(Long productId) {
        var queryString = "SELECT pp FROM ProductParameter pp JOIN FETCH pp.sensorType WHERE pp.product.id = :productId";
        var query = entityManager.createQuery(queryString, ProductParameter.class);
        query.setParameter("productId", productId);
        return query.getResultList();
    }

    //TODO: load function with excel

    //TODO: update manufacturer, update package, update isActive status. differnt endpoints?
    // qq tem de acontecer para alterar o estado do product para isActive false?
    //TODO: export
    public String export(String fileLocation) throws IOException {
        List<String> productAttributesList = this.getExportableProperties();

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
        for (int i = 0; i < productAttributesList.size(); i++){
            headerCell = header.createCell(i);
            headerCell.setCellValue(productAttributesList.get(i));
            headerCell.setCellStyle(headerStyle);
        }

        CellStyle style = workbook.createCellStyle();
        style.setWrapText(true);

        int rowNum = 1;
        List<Product> productList = getProductsForExport();

        for (Product product : productList) {
            List<Package> packages = product.getPackages();
            if(!packages.isEmpty()) {
                for (Package aPackage : packages) {
                    Row row = sheet.createRow(rowNum++);
                    this.parseProductToSheet(product, aPackage, style, row, productAttributesList);
                }
            }else{
                Row row = sheet.createRow(rowNum++);
                this.parseProductToSheet(product, null, style, row, productAttributesList);
            }
        }

        if (fileLocation == null || fileLocation.isEmpty()) {
            File currDir = new File(".");
            String path = currDir.getAbsolutePath();
            fileLocation = path.substring(0, path.length() - 1);
        }
        fileLocation = fileLocation + "temp.xlsx";

        FileOutputStream outputStream = new FileOutputStream(fileLocation);
        workbook.write(outputStream);
        workbook.close();
        return fileLocation;
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

    public Product delete(long id) throws MyEntityNotFoundException {
        Product product = this.find(id);
        entityManager.remove(product);
        return product;
    }

    private List<String> getExportableProperties() {
        List<String> productAttributesList = new ArrayList<>();

        productAttributesList.add("Reference");
        productAttributesList.add("Name");
        productAttributesList.add("Description");
        productAttributesList.add("Price");
        productAttributesList.add("Manufacturer");
        productAttributesList.add("ManufacturerEmail");
        productAttributesList.add("UnitStock");
        productAttributesList.add("BoxStock");
        productAttributesList.add("ContainerStock");
        productAttributesList.add("PackageType");
        productAttributesList.add("PackageMaterial");

        return productAttributesList;
    }

    private void parseProductToSheet(
            Product product,
            Package aPackage,
            CellStyle style,
            Row row,
            List<String> productAttributesList
    ) {
        Cell cell;
        for (int i = 0; i < productAttributesList.size(); i++){
            cell = row.createCell(i);
            setCellValue(productAttributesList.get(i), product, cell, aPackage);
            cell.setCellStyle(style);
        }

    }

    private void setCellValue(String attribute, Product product, Cell cell, Package aPackage){
        switch (attribute) {
            case "Reference":
                cell.setCellValue(product.getProductReference());
                break;
            case "Name":
                cell.setCellValue(product.getName());
                break;
            case "Description":
                cell.setCellValue(product.getDescription());
                break;
            case "Price":
                cell.setCellValue(product.getPrice());
                break;
            case "Manufacturer":
                cell.setCellValue(product.getManufacturer().getName());
                break;
            case "ManufacturerEmail":
                cell.setCellValue(product.getManufacturer().getEmail());
                break;
            case "UnitStock":
                cell.setCellValue(product.getUnitStock());
                break;
            case "BoxStock":
                cell.setCellValue(product.getBoxStock());
                break;
            case "ContainerStock":
                cell.setCellValue(product.getContainerStock());
                break;
            case "PackageType":
                if(aPackage == null){
                    cell.setCellValue("");
                    break;
                }
                cell.setCellValue(aPackage.getPackageType().getDisplayType());
                break;
            case "PackageMaterial":
                if(aPackage == null){
                    cell.setCellValue("");
                    break;
                }
                cell.setCellValue(aPackage.getMaterial());
                break;
            default:
                break;
        }
    }
}
