package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions;

import java.util.stream.Collectors;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

public class MyConstraintViolationException extends Exception{
    public MyConstraintViolationException(ConstraintViolationException e) {
        super(getConstraintViolationMessages(e));
    }
    private static String
    getConstraintViolationMessages(ConstraintViolationException e) {
        return e.getConstraintViolations()
                .stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining("; "));
    }
}
