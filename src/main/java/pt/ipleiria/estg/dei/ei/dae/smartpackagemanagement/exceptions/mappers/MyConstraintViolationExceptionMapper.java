package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.mappers;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyConstraintViolationException;

import java.util.logging.Logger;

@Provider
public class MyConstraintViolationExceptionMapper implements ExceptionMapper<MyConstraintViolationException> {
    private static final Logger logger = Logger.getLogger(MyConstraintViolationException.class.getCanonicalName());

    @Override
    public Response toResponse(MyConstraintViolationException e) {
        String errorMsg = e.getMessage();
        logger.warning("ERROR: " + errorMsg);
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(errorMsg)
                .build();
    }
}
