package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.specifications;

import java.time.Instant;
import java.util.Map;

public class GenericFilterMapBuilder {
    private final static String separator = "/_/";

    public static <T> void addToFilterMap(T param, Map<String, String> filterMap, String keyField, String operation) {
        if (param == null || (param.getClass().equals(Double.class)
                || param.getClass().equals(double.class)) && (Double) param == 0.0) {
            return;
        }
        String datatype;
        String valueField;
        if (param.getClass().equals(String.class)) {
            datatype = "String";
            valueField = (String) param;
        } else if (param.getClass().equals(Instant.class)) {
            datatype = "Instant";
            Instant instant = (Instant) param;
            valueField = instant.toString();
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
        } else {
            datatype = param.getClass().getSimpleName();
            valueField = param.toString();
        }
        String key = datatype + separator + keyField + separator + operation;
        filterMap.put(key, valueField);
    }

    public static String getSeparator() {
        return separator;
    }
}