package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.mappers;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyEntityNotFoundException;

import java.util.logging.Logger;

@Provider
public class MyEntityNotFoundExceptionMapper implements ExceptionMapper<MyEntityNotFoundException> {
    private static final Logger logger = Logger.getLogger(MyEntityNotFoundException.class.getCanonicalName());
    @Override
    public Response toResponse(MyEntityNotFoundException e) {
        String errorMsg = e.getMessage();
        logger.warning(String.format("ERROR: %s", errorMsg));
        return Response.status(Response.Status.NOT_FOUND).entity(errorMsg).build();
    }
}
