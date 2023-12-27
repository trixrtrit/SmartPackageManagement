package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@NamedQueries({
        @NamedQuery(
                name = "getLogisticsOperators",
                query = "SELECT lo FROM LogisticsOperator lo ORDER BY lo.name"
        ),
        @NamedQuery(
                name = "logisticsOperatorExists",
                query = "SELECT COUNT(lo.username) FROM LogisticsOperator lo WHERE lo.username = :username"
        )
})
public class LogisticsOperator extends User implements Serializable {

    public LogisticsOperator() {
    }

    public LogisticsOperator(String username, String password, String name, String email) {
        super(username, password, name, email);
    }
}
