package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.ejbs;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import net.datafaker.Faker;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.Package;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.Sensor;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.enums.PackageType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private ProductParameterBean productParameterBean;
    @EJB
    private SensorTypeBean sensorTypeBean;
    @EJB
    private SensorBean sensorBean;
    @EJB
    private MeasurementBean measurementBean;

    private final Faker faker = new Faker();

    private static final Map<String, String> sensorUnits = new HashMap<>();

    private static final Logger logger = Logger.getLogger("ebjs.ConfigBean");

    private int lastAssociatedSensorId = 0;
    @PostConstruct
    public void populateDB() {
        int seedSize = 100;
        int maxSensorsPerPackage = 4;
        int packageSize = seedSize/maxSensorsPerPackage;
        int measurementSize = 20;
        System.out.println("Hello Java EE!");
        seedLogOperators(seedSize);
        seedManufacturers(seedSize);
        seedProducts(seedSize);
        seedCustomers(seedSize);
        seedSensorType();
        seedProductParameters(seedSize);
        seedSensors(seedSize);
        seedPackages(packageSize, maxSensorsPerPackage);
        seedMeasurements(measurementSize);
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

    public void seedProducts(int size) {
        var manufacturers = manufacturerBean.getManufacturers();
        try {
            for (int i = 0; i < size; i++) {
                var manufacturer = manufacturers.get(faker.number().numberBetween(0, manufacturers.size()));
                long productId = productBean.create(
                        faker.commerce().productName(),
                        faker.lorem().sentence(),
                        Double.parseDouble(faker.commerce().price().replaceAll("[^\\d.]", "")),
                        manufacturer.getUsername(),
                        faker.number().digits(8),
                        faker.number().numberBetween(1,100),
                        faker.number().numberBetween(1,100),
                        faker.number().numberBetween(1,100)
                );
                var product = productBean.find(productId);
                product.setUnitStock(faker.number().numberBetween(0,1000));
                product.setBoxStock(faker.number().numberBetween(0,1000));
                product.setContainerStock(faker.number().numberBetween(0,1000));
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
                sensorTypeBean.create(mapKey, sensorUnits.get(mapKey));
            }
        } catch (Exception ex) {
            logger.severe(ex.getMessage());
        }
    }

    public void seedProductParameters(int size) {
        var products = productBean.getProducts();
        var sensorTypes = sensorTypeBean.getProductParameters();
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
        var sensorTypes = sensorTypeBean.getProductParameters();
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
        }
    }

    private void seedPackages(int size, int maxSensorsPerPackage) {
        var sensors = sensorBean.getSensors();
        var products = productBean.getProducts();
        var packTypes = PackageType.values();
        int packTypesLength = packTypes.length;
        try {
            for (int i = 0; i < size; i++) {
                int numberOfSensors = faker.number().numberBetween(1, maxSensorsPerPackage);
                var packType = packTypes[faker.number().numberBetween(0, packTypesLength)];

                long packId = packageBean.create(
                        faker.number().randomNumber(9, true),
                        faker.commerce().material(),
                        packType
                );
                for (int j = 0; j < numberOfSensors; j++) {
                    packageBean.addSensorToPackage(packId, sensors.get(lastAssociatedSensorId).getId());
                    lastAssociatedSensorId++;
                }
                packageBean.addProductToPackage(packId, products.get(i).getId());
            }
        } catch (Exception ex) {
            logger.severe(ex.getMessage());
        }
    }

    private void seedMeasurements(int size) {
        var packages = packageBean.getPackages();
         try {
             for (Package aPackage: packages) {
                var sensors = packageBean.findPackageCurrentSensors(aPackage.getCode());
                for(Sensor sensor: sensors) {
                    for (int i = 0; i < 20; i++) {
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
         }
     }


}
