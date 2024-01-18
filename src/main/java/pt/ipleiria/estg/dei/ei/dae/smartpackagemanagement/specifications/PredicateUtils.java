package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.specifications;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class PredicateUtils {
    public static <T, E extends Enum<E>> Predicate handleEnumPredicate(String enumName,
            String fieldName, String enumValue,  String operation, CriteriaBuilder builder, Root<T> root) {
        try {
            String packageName = "pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.enums";
            Class<E> enumClass = (Class<E>) Class.forName(packageName + "." + enumName);
            E enumConstant = Enum.valueOf(enumClass, enumValue.toUpperCase());

            if ("notEqual".equalsIgnoreCase(operation)) {
                return builder.notEqual(root.get(fieldName), enumConstant);
            } else { // Default to "equal"
                return builder.equal(root.get(fieldName), enumConstant);
            }
        } catch (ClassNotFoundException | IllegalArgumentException e) {
            throw new RuntimeException("Enum handling error: " + e.getMessage(), e);
        }
    }
}
