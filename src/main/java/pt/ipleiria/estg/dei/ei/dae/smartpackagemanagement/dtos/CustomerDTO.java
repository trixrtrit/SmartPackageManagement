package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.dtos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CustomerDTO implements Serializable {
    private String username;
    private String password;
    private String email;
    private String name;
    private String nif;
    private String address;
    private List<OrderDTO> ordersDTO;

    public CustomerDTO() {
        this.ordersDTO = new ArrayList<>();
    }

    public CustomerDTO(String username, String password, String email, String name, String nif, String address) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.name = name;
        this.nif = nif;
        this.address = address;
        this.ordersDTO = new ArrayList<>();
    }

    public CustomerDTO(String username, String password, String email, String name, String nif, String address, List<OrderDTO> ordersDTO) {
        this(username, password, email, name, nif, address);
        this.ordersDTO = ordersDTO;
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

    public String getNif() {
        return nif;
    }

    public void setNif(String nif) {
        this.nif = nif;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<OrderDTO> getOrdersDTO() {
        return ordersDTO;
    }

    public void setOrdersDTO(List<OrderDTO> ordersDTO) {
        this.ordersDTO = ordersDTO;
    }
}
