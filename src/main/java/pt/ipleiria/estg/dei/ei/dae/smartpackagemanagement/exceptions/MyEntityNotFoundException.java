package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions;

public class MyEntityNotFoundException extends Exception{
    private String message;

    public MyEntityNotFoundException(String message) {
        this.message = message;
    }
}
