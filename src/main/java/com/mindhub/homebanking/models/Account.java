package com.mindhub.homebanking.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"client"})
@ToString(exclude = {"client"})
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native",strategy = "native")
    private long id;

    private String accountType;
    private String number;
    private LocalDateTime creationDate;
    private double balance;
    private boolean active;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="client_Id")
    private Client client;

    @OneToMany(mappedBy="account", fetch=FetchType.EAGER)
    private Set<Transaction> transactions = new HashSet<>();


    public Account(String accountType, String number, LocalDateTime creationDate, double balance, boolean active, Client client) {
        this.number = number;
        this.creationDate = creationDate;
        this.balance = balance;
        this.client= client;
        this.accountType = accountType;
        this.active = active;
    }


}
