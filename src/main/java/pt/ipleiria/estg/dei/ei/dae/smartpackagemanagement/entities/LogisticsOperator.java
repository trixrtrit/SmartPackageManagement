package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@NamedQueries({
        @NamedQuery(
                name = "getLogisticsOperators",
                query = "SELECT lo FROM LogisticsOperator lo ORDER BY lo.name"
        ),
        @NamedQuery(
                name = "logisticsOperatorsExists",
                query = "SELECT COUNT(lo.username) FROM LogisticsOperator lo WHERE lo.username = :username"
        )
})
public class LogisticsOperator extends User implements Serializable {
    @OneToMany(mappedBy = "logisticsOperator", cascade = CascadeType.REMOVE)
    private List<Package> packages;

    public LogisticsOperator() {
        this.packages = new ArrayList<Package>();
    }

    public LogisticsOperator(String username, String password, String name, String email) {
        super(username, password, name, email);
        this.packages = new ArrayList<Package>();
    }

    public List<Package> getPackages() {
        return packages;
    }

    public void setPackages(List<Package> packages) {
        this.packages = packages;
    }
}
