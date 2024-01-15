package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.dtos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ManufacturerDTO implements Serializable {
    private String username;
    private String password;
    private String email;
    private String name;
    private List<ProductDTO> products;

    public ManufacturerDTO() {
        this.products = new ArrayList<>();
    }

    public ManufacturerDTO(String username, String password, String email, String name) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.name = name;
        this.products = new ArrayList<>();
    }

    public ManufacturerDTO(
            String username,
            String password,
            String email,
            String name,
            List<ProductDTO> products
    ) {
        this(username, password, email, name);
        this.products = products;
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

    public List<ProductDTO> getProducts() {
        return products;
    }

    public void setProducts(List<ProductDTO> products) {
        this.products = products;
    }
}
