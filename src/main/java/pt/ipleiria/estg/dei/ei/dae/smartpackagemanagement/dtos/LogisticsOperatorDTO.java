package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.dtos;

import java.util.ArrayList;
import java.util.List;

public class LogisticsOperatorDTO {
    private String username;
    private String password;
    private String email;
    private String name;
    private List<PackageDTO> packages;

    public LogisticsOperatorDTO() {
        this.packages = new ArrayList<>();
    }

    public LogisticsOperatorDTO(String username, String password, String email, String name) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.name = name;
        this.packages = new ArrayList<>();
    }

    public LogisticsOperatorDTO(String username, String password, String email, String name, List<PackageDTO> packages) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.name = name;
        this.packages = packages;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<PackageDTO> getPackages() {
        return packages;
    }

    public void setPackages(List<PackageDTO> packages) {
        this.packages = packages;
    }
}
