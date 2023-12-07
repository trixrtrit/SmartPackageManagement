package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions;

public class MyEntityExistsException extends Exception{
    private String message;

    public MyEntityExistsException(String message) {
        this.message = message;
    }
}
