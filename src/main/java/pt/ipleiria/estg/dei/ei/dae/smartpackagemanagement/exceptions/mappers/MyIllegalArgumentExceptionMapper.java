package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.mappers;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.logging.Logger;

@Provider
public class MyIllegalArgumentExceptionMapper implements ExceptionMapper<IllegalArgumentException> {
    private static final Logger logger = Logger.getLogger(IllegalArgumentException.class.getCanonicalName());
    @Override
    public Response toResponse(IllegalArgumentException e) {
        String errorMsg = e.getMessage();
        logger.warning(String.format("ERROR: %s", errorMsg));
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorMsg).build();
    }
}
