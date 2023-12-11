package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.dtos;

import java.util.ArrayList;
import java.util.List;

public class ManufacturerDTO {
    private String username;
    private String password;
    private String email;
    private String name;
    private List<PackageDTO> packagesDTO;
    private List<ProductDTO> productsDTO;

    public ManufacturerDTO() {
        this.packagesDTO = new ArrayList<>();
        this.productsDTO = new ArrayList<>();
    }

    public ManufacturerDTO(String username, String password, String email, String name) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.name = name;
        this.packagesDTO = new ArrayList<>();
        this.productsDTO = new ArrayList<>();
    }

    public ManufacturerDTO(
            String username,
            String password,
            String email,
            String name,
            List<PackageDTO> packagesDTO,
            boolean hasProduct
    ) {
        this(username, password, email, name);
        this.packagesDTO = packagesDTO;
        this.productsDTO = new ArrayList<>();
    }

    public ManufacturerDTO(
            String username,
            String password,
            String email,
            String name,
            List<ProductDTO> productsDTO
    ) {
        this(username, password, email, name);
        this.productsDTO = productsDTO;
        this.packagesDTO = new ArrayList<>();
    }

    public ManufacturerDTO(
            String username,
            String password,
            String email,
            String name,
            List<PackageDTO> packagesDTO,
            List<ProductDTO> productsDTO) {
        this(username, password, email, name);
        this.packagesDTO = packagesDTO;
        this.productsDTO = productsDTO;
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

    public List<PackageDTO> getPackagesDTO() {
        return packagesDTO;
    }

    public void setPackagesDTO(List<PackageDTO> packagesDTO) {
        this.packagesDTO = packagesDTO;
    }

    public List<ProductDTO> getProductsDTO() {
        return productsDTO;
    }

    public void setProductsDTO(List<ProductDTO> productsDTO) {
        this.productsDTO = productsDTO;
    }
}
