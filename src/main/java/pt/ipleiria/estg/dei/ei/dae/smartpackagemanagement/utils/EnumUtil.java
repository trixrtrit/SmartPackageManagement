package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.utils;

public class EnumUtil {
    public static <E extends Enum<E>> E getEnumFromString(Class<E> enumClass, String enumString) {
        if (enumString == null) {
            return null;
        }
        try {
            return Enum.valueOf(enumClass, enumString.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid value for enum " + enumClass.getSimpleName() + ": " + enumString);
        }
    }
}
