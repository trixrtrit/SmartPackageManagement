package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.mappers;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyValidationException;

import java.util.logging.Logger;

@Provider
public class MyValidationExceptionMapper implements ExceptionMapper<MyValidationException> {
    private static final Logger logger = Logger.getLogger(MyValidationException.class.getCanonicalName());

    @Override
    public Response toResponse(MyValidationException e) {
        String errorMsg = e.getMessage();
        logger.warning(String.format("ERROR: %s", errorMsg));
        return Response.status(Response.Status.BAD_REQUEST).entity(errorMsg).build();
    }
}
