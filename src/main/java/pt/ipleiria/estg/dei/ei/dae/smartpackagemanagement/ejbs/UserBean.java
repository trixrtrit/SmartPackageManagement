package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.ejbs;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;
import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.User;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.security.Hasher;

import java.util.Objects;

@Stateless
public class UserBean {
    @PersistenceContext
    private EntityManager entityManager;

    @Inject
    private Hasher hasher;

    //TODO: exceptions
    public User find(String username) throws MyEntityNotFoundException{
        User user = entityManager.find(User.class, username);
        if (user == null) {
            throw new MyEntityNotFoundException("The user with the username: " + username + " does not exist");
        }
        return user;
    }
    public User findOrFail(String username) {
        var user = entityManager.getReference(User.class, username);
        Hibernate.initialize(user);
        return user;
    }
    public boolean canLogin(String username, String password) throws MyEntityNotFoundException{
        var user = find(username);
        return user != null && user.getPassword().equals(hasher.hash(password));
    }

    @Transactional
    public void updatePassword(User user, String newPassword, String confirmationPassword){
        if (!Objects.equals(newPassword, confirmationPassword)){
            throw new BadRequestException("New password doesn't match confirmation password.");
        }
        if(Objects.equals(user.getPassword(), hasher.hash(newPassword))){
            throw new BadRequestException("New password mustn't match current password.");
        }
        if(!isStrongPassword(newPassword)) {
            throw new BadRequestException("New password must be at least 6 characters long, have an uppercase letter, a lowercase letter and a special symbol.");
        }
        User mergedUser = entityManager.merge(user);
        entityManager.lock(mergedUser, LockModeType.OPTIMISTIC);
        mergedUser.setPassword(hasher.hash(newPassword));
    }

    private boolean isStrongPassword(String password) {
        if (password.length() < 6) {
            return false;
        }

        // Check for at least one digit
        if (!password.matches(".*\\d.*")) {
            return false;
        }

        // Check for at least one uppercase letter
        if (!password.matches(".*[A-Z].*")) {
            return false;
        }

        // Check for at least one lowercase letter
        if (!password.matches(".*[a-z].*")) {
            return false;
        }

        // Check for at least one special character
        return password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\",.<>?/\\\\|].*");
    }
}
