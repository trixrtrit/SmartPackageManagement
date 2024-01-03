package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.ejbs;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import net.datafaker.Faker;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.enums.PackageType;

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

    private final Faker faker = new Faker();

    private static final Logger logger = Logger.getLogger("ebjs.ConfigBean");
    @PostConstruct
    public void populateDB() {
        int seedSize = 100;
        System.out.println("Hello Java EE!");
        seedLogOperators(seedSize);
        seedManufacturers(seedSize);
        seedProducts(seedSize);
        seedCustomers(seedSize);
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
                var manufacturer = manufacturers.get(faker.number().numberBetween(0, manufacturers.size()-1));
                long productId = productBean.create(
                        faker.commerce().productName(),
                        faker.lorem().sentence(),
                        Double.parseDouble(faker.commerce().price().replaceAll("[^\\d.]", "")),
                        manufacturer.getUsername(),
                        faker.number().digits(8)
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
    //TODO: package/productparameter/sensortype
}
