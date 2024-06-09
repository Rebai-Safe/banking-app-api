package com.bankingApp.dto;

import com.bankingApp.entity.BankAccount;
import com.bankingApp.enums.OperationType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import lombok.Data;

import java.util.Date;

@Data
public class AccountOperationDto {

    private Long id;
    private Date operationDate;
    private double amount;
    private String description;
    private OperationType operationType;

}
