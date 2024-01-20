package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.specifications;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.logging.Logger;

public class GenericFilterMapBuilder {
    private final static String separator = "/_/";
    private final static String enumSeparator = "/__/";

    public static <T> void addToFilterMap(T param, Map<String, String> filterMap, String keyField, String operation) {
        if (param == null || (param instanceof Number && ((Number) param).doubleValue() == 0.0)) {
            return;
        }

        String datatype;
        String valueField;
        if (param instanceof Enum<?>) {
            datatype = "Enum" + enumSeparator + param.getClass().getSimpleName();
            valueField = ((Enum<?>) param).name();
        } else if (param.getClass().equals(String.class)) {
            datatype = "String";
            valueField = (String) param;
        } else if (param.getClass().equals(Date.class)) {
            datatype = "Date";
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            valueField = formatter.format(param);
        } else if (param.getClass().equals(Double.class) || param.getClass().equals(double.class)) {
            datatype = "Double";
            valueField = param.toString();
        } else if (param.getClass().equals(Integer.class) || param.getClass().equals(int.class)) {
            datatype = "Integer";
            valueField = param.toString();
        } else if (param.getClass().equals(Long.class) || param.getClass().equals(long.class)) {
            datatype = "Long";
            valueField = param.toString();
        } else if (param.getClass().equals(Boolean.class) || param.getClass().equals(boolean.class)) {
            datatype = "Boolean";
            valueField = param.toString();
        }
        else {
            datatype = param.getClass().getSimpleName();
            valueField = param.toString();
        }

        if(operation.equals("Manufacturer")){
            datatype = "ManufacturerPackage";
        } else if (operation.equals("Customer")) {
            datatype = "CustomerPackage";
        }

        String key = datatype + separator + keyField + separator + operation;
        Logger logger = Logger.getLogger("GenericFilterMapBuilder");
        logger.severe(key);
        logger.severe(valueField);
        filterMap.put(key, valueField);
    }

    public static String getSeparator() {
        return separator;
    }

    public static String getEnumSeparator() { return enumSeparator; }
}