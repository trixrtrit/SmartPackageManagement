package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.mappers;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyIllegalConstraintException;

import java.util.logging.Logger;

@Provider
public class MyIllegalConstraintExceptionMapper implements ExceptionMapper<MyIllegalConstraintException> {
    private static final Logger logger = Logger.getLogger(MyIllegalConstraintException.class.getCanonicalName());
    @Override
    public Response toResponse(MyIllegalConstraintException e) {
        String errorMsg = e.getMessage();
        logger.warning(String.format("ERROR: %s", errorMsg));
        return Response.status(Response.Status.BAD_REQUEST).entity(errorMsg).build();
    }
}
