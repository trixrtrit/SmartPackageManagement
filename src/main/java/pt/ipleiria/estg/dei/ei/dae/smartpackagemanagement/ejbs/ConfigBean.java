package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.ejbs;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import net.datafaker.Faker;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.Package;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.Sensor;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.enums.MeasurementType;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.enums.PackageType;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyEntityNotFoundException;

import java.util.*;
import java.util.logging.Logger;

@Startup
@Singleton
public class ConfigBean {
    @EJB
    private LogisticsOperatorBean logisticsOperatorBean;
    @EJB
    private ManufacturerBean manufacturerBean;
    @EJB
    private CustomerBean customerBean;
    @EJB
    private ProductBean productBean;
    @EJB
    private PackageBean packageBean;
    @EJB
    private StandardPackageBean standardPackageBean;
    @EJB
    private TransportPackageBean transportPackageBean;
    @EJB
    private ProductParameterBean productParameterBean;
    @EJB
    private SensorTypeBean sensorTypeBean;
    @EJB
    private SensorBean sensorBean;
    @EJB
    private MeasurementBean measurementBean;
    @EJB
    private PrimaryPackageTypeBean primaryPackageTypeBean;
    @EJB
    private PrimaryPackageMeasurementUnitBean primaryPackageMeasurementUnitBean;
    @EJB
    private ProductCategoryBean productCategoryBean;

    private final Faker faker = new Faker();

    private static final Map<String, String> sensorUnits = new HashMap<>();

    private static final Logger logger = Logger.getLogger("ebjs.ConfigBean");

    private int lastAssociatedSensorId = 0;
    @PostConstruct
    public void populateDB() throws MyConstraintViolationException, MyEntityNotFoundException, MyEntityExistsException {
        int seedSize = 100;
        int maxSensorsPerPackage = 2;
        int measurementSize = 10;
        int factor = 5;

        System.out.println("Hello Java EE!");

        try {
            logisticsOperatorBean.create(
                    "gatoMega",
                    "123",
                    "gatoMega",
                    "gatoMega@mail.pt"
            );
            manufacturerBean.create(
                    "gatoFabricante",
                    "123",
                    "gatoFabricante",
                    "gatoFabricante@mail.pt"
            );
            customerBean.create(
                    "gatoCliente",
                    "123",
                    "gatoCliente",
                    "gatoCliente@mail.pt",
                    "999999999",
                    "GatoLandia"
            );
        }
        catch (Exception ex) {
            logger.severe(ex.getMessage());
        }

        seedLogOperators(seedSize);
        seedManufacturers(seedSize);
        seedPrimaryPackageTypes();
        seedPrimaryPackageMeasurementUnits();
        seedProductCategories();
        seedProducts(seedSize);
        seedCustomers(seedSize);
        seedSensorType();
        seedProductParameters(seedSize);
        seedSensors(seedSize * factor * maxSensorsPerPackage);
        seedPackages(seedSize * factor, maxSensorsPerPackage, factor);
        seedTransportPackages(seedSize, maxSensorsPerPackage);
        seedMeasurements(measurementSize);
    }

    public void seedLogOperators(int size) {
        try {
            for (int i = 0; i < size; i++) {
                String firstName = faker.name().firstName();
                String lastName = faker.name().lastName();
                String username = firstName.toLowerCase()+"."+lastName.toLowerCase();
                logisticsOperatorBean.create(
                        username,
                        "123",
                        firstName + " " + lastName,
                        faker.internet().emailAddress(username)
                );
            }
        } catch (Exception ex) {
            logger.severe(ex.getMessage());
        }
    }

    public void seedManufacturers(int size) {
        try {
            for (int i = 0; i < size; i++) {
                String firstName = faker.name().firstName();
                String lastName = faker.name().lastName();
                String username = firstName.toLowerCase()+"."+lastName.toLowerCase();
                manufacturerBean.create(
                        username,
                        "123",
                        firstName + " " + lastName,
                        faker.internet().emailAddress(username)
                );
            }
        } catch (Exception ex) {
            logger.severe(ex.getMessage());
        }
    }

    public void seedPrimaryPackageTypes() throws MyConstraintViolationException, MyEntityNotFoundException, MyEntityExistsException {
        var packageTypes = new String[]{
                "Small Cardboard Container",
                "Small Plastic Container",
                "Bottle",
                "Bag",
                "Small Barrel"
        };

        for (var packageType : packageTypes) {
            primaryPackageTypeBean.create(packageType);
        }
    }

    public void seedPrimaryPackageMeasurementUnits() throws MyConstraintViolationException, MyEntityNotFoundException, MyEntityExistsException {
        var measurementUnits = new String[]{
                "kg",
                "liter(s)",
                "meter(s)",
                "unit(s)"
        };

        for (var measurementUnit : measurementUnits) {
            primaryPackageMeasurementUnitBean.create(measurementUnit);
        }
    }

    public void seedProductCategories() throws MyConstraintViolationException, MyEntityNotFoundException, MyEntityExistsException {
        var productCategories = new String[]{
                "Food",
                "Electronics",
                "Clothing",
                "Footwear",
                "Beauty Products",
                "Groceries",
                "Home Appliances",
                "Furniture",
                "Toys & Games",
                "Books",
                "Sports Equipment",
                "Jewelry",
                "Health & Wellness",
                "Gardening Supplies",
                "Pet Supplies",
                "Office Supplies",
                "Musical Instruments",
                "Automotive Accessories",
                "Bakery Items",
                "Frozen Foods",
                "Dairy Products",
                "Snacks & Confectionery",
                "Beverages",
                "Baby Products",
                "Art Supplies",
                "Photography Equipment",
                "Travel Accessories",
                "DIY Tools",
                "Computer Software",
                "Video Games",
                "Mobile Accessories"
        };

        for (var productCategory : productCategories) {
            productCategoryBean.create(productCategory);
        }
    }

    public void seedProducts(int size) {
        var manufacturers = manufacturerBean.getManufacturers(new HashMap<String, String>(), 1, size);
        var primaryPackageTypes = primaryPackageTypeBean.getTypes();
        var primaryPackageUnits = primaryPackageMeasurementUnitBean.getUnits();
        var productCategories = productCategoryBean.getCategories();

        try {
            for (int i = 0; i < size; i++) {
                var manufacturer = manufacturers.get(faker.number().numberBetween(0, manufacturers.size()));
                var primaryPackageType = primaryPackageTypes.get(faker.number().numberBetween(0, primaryPackageTypes.size()));
                var primaryPackageUnit = primaryPackageUnits.get(faker.number().numberBetween(0, primaryPackageUnits.size()));
                var productCategory = productCategories.get(faker.number().numberBetween(0, productCategories.size()));
                //System.out.println("seedProducts:" + i);
                long productId = productBean.create(
                        faker.commerce().productName(),
                        faker.lorem().sentence(),
                        Double.parseDouble(faker.commerce().price().replaceAll("[^\\d.]", "")),
                        manufacturer.getUsername(),
                        faker.number().digits(8),
                        primaryPackageUnit.getId(),
                        primaryPackageType.getId(),
                        productCategory.getId(),
                        faker.number().numberBetween(1,100),
                        faker.number().numberBetween(1,100),
                        faker.number().numberBetween(1,100)
                );
//                var product = productBean.find(productId);
//                product.setUnitStock(faker.number().numberBetween(0,1000));
//                product.setBoxStock(faker.number().numberBetween(0,1000));
//                product.setContainerStock(faker.number().numberBetween(0,1000));
            }
        } catch (Exception ex) {
            logger.severe(ex.getMessage());
        }
    }

    public void seedCustomers(int size) {
        try {
            for (int i = 0; i < size; i++) {
                String firstName = faker.name().firstName();
                String lastName = faker.name().lastName();
                String username = firstName.toLowerCase()+"."+lastName.toLowerCase();
                customerBean.create(
                        username,
                        "123",
                        firstName + " " + lastName,
                        faker.internet().emailAddress(username),
                        faker.number().digits(9),
                        faker.address().fullAddress()
                );
            }
        } catch (Exception ex) {
            logger.severe(ex.getMessage());
        }
    }

    public void seedSensorType() {
        sensorUnits.put("Temperature", "°C");
        sensorUnits.put("Pressure", "Pa");
        sensorUnits.put("Humidity", "%");
        sensorUnits.put("Proximity", "cm");
        List<String> mapKeys = new ArrayList<>(sensorUnits.keySet());

        try {
            for (String mapKey : mapKeys) {
                sensorTypeBean.create(mapKey, sensorUnits.get(mapKey), MeasurementType.NUMERIC);
            }
        } catch (Exception ex) {
            logger.severe(ex.getMessage());
        }
    }

    public void seedProductParameters(int size) {
        var products = productBean.getProducts(new HashMap<String, String>(), 1, size);
        var sensorTypes = sensorTypeBean.getProductParameters(new HashMap<String, String>(), 1, size);
        try {
            int count = 0;
            while (count < size) {
                float threshold1 = (float) (0 + Math.random() * (100));
                float threshold2 = (float) (0 + Math.random() * (100));
                var product = products.get(faker.number().numberBetween(0, products.size()));
                var sensorType = sensorTypes.get(faker.number().numberBetween(0, sensorTypes.size()));
                if(!productParameterBean.exists(product.getId(), sensorType.getId())) {
                    productParameterBean.create(
                            product.getId(),
                            sensorType.getId(),
                            Math.min(threshold1, threshold2),
                            Math.max(threshold1, threshold2)
                    );
                    count++;
                }
            }
        } catch (Exception ex) {
            logger.severe(ex.getMessage());
        }
    }

    private void seedSensors(int size) {
        var sensorTypes = sensorTypeBean.getProductParameters(new HashMap<String, String>(), 1, size);
        Map<String, Integer> sensorUnitCount = new HashMap<>();
        try {
            int count = 0;
            int unitCount;
            while (count < size) {
                var sensorType = sensorTypes.get(faker.number().numberBetween(0, sensorTypes.size()));
                if(!sensorUnitCount.containsKey(sensorType.getName())){
                    sensorUnitCount.put(sensorType.getName(), 1);
                }
                unitCount = sensorUnitCount.get(sensorType.getName());
                sensorBean.create(sensorType.getName() + "Sensor" + unitCount, sensorType.getId());
                unitCount++;
                sensorUnitCount.put(sensorType.getName(), unitCount);
                count++;
            }
        } catch (Exception ex) {
            logger.severe(ex.getMessage());
            logger.severe("seedSensors");
        }
    }

    private void seedPackages(int size, int maxSensorsPerPackage, int factor) {
        int packageSize = size;
        var sensors = sensorBean.getSensors(new HashMap<String, String>(), 1, size * maxSensorsPerPackage);
        var products = productBean.getProducts(new HashMap<String, String>(), 1, size/factor);
        var listOfProductIds = products.stream().map(product -> product.getId()).toArray();
        var packTypes = PackageType.values();
        int packTypesLength = packTypes.length;
        try {
            for (int i = 0; i < packageSize; i++) {
                int numberOfSensors = faker.number().numberBetween(1, maxSensorsPerPackage);
                int packageTypeNumber = faker.number().numberBetween(0, packTypesLength);
                var packType = packTypes[packageTypeNumber];
                long productNumber = (long) listOfProductIds[faker.number().numberBetween(0, listOfProductIds.length)];
                //System.out.println("seed packages iteration:[" + i + "] for productId:[" + productNumber + "]");
                long packId = standardPackageBean.create(
                        faker.number().randomNumber(9, true),
                        faker.commerce().material(),
                        packType,
                        new Date(),
                        productNumber

                );
                //todo assign sensors
                for (int j = 0; j < numberOfSensors; j++) {
                    try {
                        standardPackageBean.addSensorToPackage(packId, sensors.get(lastAssociatedSensorId).getId());
                        lastAssociatedSensorId++;
                    } catch (Exception ex) {
                        logger.severe(ex.getMessage());
                        logger.severe("addSensorToPackage");
                    }
                }
                standardPackageBean.removeSensorFromPackage(packId,sensors.get(lastAssociatedSensorId - numberOfSensors).getId());
                //standardPackageBean.addProductToPackage(packId, products.get(i).getId());
            }
        } catch (Exception ex) {
            logger.severe(ex.getMessage());
            logger.severe("seedPackages");
        }
    }

    private void seedTransportPackages(int size, int maxSensorsPerPackage) {
        var sensors = sensorBean.getSensors(new HashMap<String, String>(), 1, size*30);
        var standardPkgs = standardPackageBean.getStandardPackages(new HashMap<String, String>(), 1, size*10);
        int stdPkgsToTransport = 5;
        int packageSize = standardPkgs.size()/stdPkgsToTransport/10;
        int lastAssociatedPkgCode = 0;
        try {
            for (int i = 0; i < packageSize; i++) {
                int numberOfSensors = faker.number().numberBetween(1, maxSensorsPerPackage);
                long packId = transportPackageBean.create(
                        faker.number().randomNumber(9, true),
                        faker.commerce().material()
                );
                for (int j = 0; j < numberOfSensors; j++) {
                    transportPackageBean.addSensorToPackage(packId, sensors.get(lastAssociatedSensorId).getId());
                    lastAssociatedSensorId++;
                }
                for (int k = 0; k < stdPkgsToTransport; k++) {
                    transportPackageBean.addStandardPkgToTransportPkg(
                            packId,
                            standardPkgs.get(lastAssociatedPkgCode).getCode()
                    );
                    lastAssociatedPkgCode++;
                }
                transportPackageBean.removeSensorFromPackage(
                        packId,
                        sensors.get(lastAssociatedSensorId - numberOfSensors).getId()
                );
                transportPackageBean.removeStandardPkgFromTransportPkg(
                        packId,
                        standardPkgs.get(lastAssociatedPkgCode - stdPkgsToTransport).getCode()
                );
            }
        } catch (Exception ex) {
            logger.severe(ex.getMessage());
            logger.severe("seedTransportPackages");
        }
    }

    private void seedMeasurements(int size) {
        var packages = packageBean.getPackages(new HashMap<String, String>(), 1, 100);
        try {
            for (Package aPackage: packages) {
                var sensors = packageBean.findPackageCurrentSensors(aPackage.getCode());
                for(Sensor sensor: sensors) {
                    for (int i = 0; i < 11; i++) {
                        measurementBean.create(
                                Double.toString(faker.number().randomDouble(3,0,100)),
                                aPackage.getCode(),
                                sensor.getId()
                        );
                    }
                }
            }
        } catch (Exception ex) {
            logger.severe(ex.getMessage());
            logger.severe("seedMeasurements");
        }
    }
}
