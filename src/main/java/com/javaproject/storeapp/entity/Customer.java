package com.javaproject.storeapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "firstName")
    private String firstName;
    @Column(name = "lastName")
    private String lastName;
    private String mail;
    private String address;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<Order> orders;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<BankAccount> bankAccounts;

    @OneToOne(mappedBy = "customer", fetch = FetchType.LAZY)
    // @JsonIgnore
    private Cart cart;

    public Customer(int id, String firstName, String lastName, String mail, String address) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.mail = mail;
        this.address = address;
    }

    public Customer(String firstName, String lastName, String mail, String address) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.mail = mail;
        this.address = address;
    }

    public Customer() {
    }


}
