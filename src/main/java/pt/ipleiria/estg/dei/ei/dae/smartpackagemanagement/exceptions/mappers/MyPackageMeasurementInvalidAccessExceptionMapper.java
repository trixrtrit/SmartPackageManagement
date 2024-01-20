package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.mappers;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyPackageMeasurementInvalidAccessException;

import java.util.logging.Logger;

@Provider
public class MyPackageMeasurementInvalidAccessExceptionMapper
        implements ExceptionMapper<MyPackageMeasurementInvalidAccessException> {

    private static final Logger logger =
            Logger.getLogger(MyPackageMeasurementInvalidAccessException.class.getCanonicalName());
    @Override
    public Response toResponse(MyPackageMeasurementInvalidAccessException e) {
        String errorMsg = e.getMessage();
        logger.warning(String.format("ERROR: %s", errorMsg));
        return Response.status(Response.Status.UNAUTHORIZED).entity(errorMsg).build();
    }

}
