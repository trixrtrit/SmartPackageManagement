package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.mappers;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyPackageProductAssociationViolationException;

import java.util.logging.Logger;

@Provider
public class MyPackageProductAssociationViolationExceptionMapper
        implements ExceptionMapper<MyPackageProductAssociationViolationException> {
    private static final Logger logger = Logger.getLogger(
            MyPackageProductAssociationViolationException.class.getCanonicalName()
    );
    @Override
    public Response toResponse(MyPackageProductAssociationViolationException e) {
        String errorMsg = e.getMessage();
        logger.warning(String.format("ERROR: %s", errorMsg));
        return Response.status(Response.Status.NOT_FOUND).entity(errorMsg).build();
    }
}