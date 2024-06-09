package com.bankingApp.entity;

import com.bankingApp.enums.AccountStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor

@Data

//utiliser la strategie single table
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
//definir la column type qui permet de dissocier entre compte courant et compte epargne
@DiscriminatorColumn(name = "type", length = 4)
@Entity
public class BankAccount {

    @Id
    private String id;
    private double balance;
    private Date createdAt;
    @Enumerated(EnumType.STRING) //stocker l'enumeration en format string
    private AccountStatus status;

    @ManyToOne
    private Customer customer;

    @OneToMany(mappedBy = "bankAccount", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<AccountOperation> accountOperations;
}
