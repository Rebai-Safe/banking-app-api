package com.bankingApp.entity;

import com.bankingApp.enums.OperationType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.Date;
@AllArgsConstructor
@NoArgsConstructor

@Data

@Entity
public class AccountOperation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date operationDate;
    private double amount;
    private String description;
    @Enumerated(EnumType.STRING)
    private OperationType operationType;

    @ManyToOne
    private BankAccount bankAccount;
}
