package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.assemblers;

import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.dtos.CustomerDTO;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.Customer;

import java.util.List;
import java.util.stream.Collectors;

public class CustomerAssembler {

    public static CustomerDTO from(Customer customer) {
        return new CustomerDTO(
                customer.getUsername(),
                customer.getPassword(),
                customer.getEmail(),
                customer.getName(),
                customer.getNif(),
                customer.getAddress()
        );
    }

    public static List<CustomerDTO> from(List<Customer> customers) {
        return customers.stream().map(CustomerAssembler::from).collect(Collectors.toList());
    }

    public static CustomerDTO fromWithOrders(Customer customer) {
        return new CustomerDTO(
                customer.getUsername(),
                customer.getPassword(),
                customer.getEmail(),
                customer.getName(),
                customer.getNif(),
                customer.getAddress(),
                OrderAssembler.from(customer.getOrders())
        );
    }

    public static List<CustomerDTO> fromWithOrders(List<Customer> customers) {
        return customers.stream().map(CustomerAssembler::fromWithOrders).collect(Collectors.toList());
    }

}
