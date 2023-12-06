package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
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
