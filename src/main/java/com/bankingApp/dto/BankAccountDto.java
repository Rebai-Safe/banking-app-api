package com.bankingApp.dto;

import com.bankingApp.entity.Customer;
import com.bankingApp.enums.AccountStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;



@Data
public class BankAccountDto {

    private String id;
    private String type;
    private double balance;
    private Date createdAt;
    private AccountStatus status;
    private CustomerDto customerDto;
}
