package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "orders")
public class Order extends Versionable{

    @Id
    private long code;
    @ManyToOne
    @JoinColumn(name = "customer_username")
    @NotNull
    private Customer customer;
}
