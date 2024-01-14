package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.assemblers;

import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.dtos.UserDTO;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.User;

import java.util.List;
import java.util.stream.Collectors;

public class UserAssembler {
    public static UserDTO from(User user) {
        return new UserDTO(
                user.getUsername(),
                user.getName(),
                user.getEmail(),
                Hibernate.getClass(user).getSimpleName()
        );
    }

    public static List<UserDTO> from(List<User> users) {
        return users.stream().map(UserAssembler::from).collect(Collectors.toList());
    }
}
