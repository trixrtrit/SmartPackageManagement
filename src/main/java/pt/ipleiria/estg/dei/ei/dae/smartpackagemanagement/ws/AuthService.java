package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.ws;

import jakarta.ejb.EJB;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.assemblers.UserAssembler;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.dtos.AuthDTO;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.dtos.ChangePasswordDTO;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.ejbs.UserBean;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.User;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.security.Authenticated;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.security.TokenIssuer;

@Path("auth")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class AuthService {
    @Inject
    private TokenIssuer tokenIssuer;
    @EJB
    private UserBean userBean;

    @Context
    private SecurityContext securityContext;

    @POST
    @Path("/login")
    public Response authenticate(@Valid AuthDTO auth) throws MyEntityNotFoundException{
        if (userBean.canLogin(auth.getUsername(), auth.getPassword())) {
            String token = tokenIssuer.issue(auth.getUsername());
            return Response.ok(token).build();
        }
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }

    @PUT
    @Path("/set-password")
    @Authenticated
    public Response changePassword(@Valid ChangePasswordDTO changePasswordDTO) throws
            MyEntityNotFoundException, BadRequestException {
        User user = this.getCurrentUser();
        userBean.updatePassword(user, changePasswordDTO.getNewPassword(), changePasswordDTO.getConfirmPassword());
        return Response.status(Response.Status.OK).build();
    }

    private User getCurrentUser() throws
            MyEntityNotFoundException{
        String username = securityContext.getUserPrincipal().getName();
        return userBean.find(username);
    }

    @GET
    @Authenticated
    @Path("/user")
    public Response getAuthenticatedUser() {
        var username = securityContext.getUserPrincipal().getName();
        var user = userBean.findOrFail(username);
        return Response.ok(UserAssembler.from(user)).build();
    }
}
