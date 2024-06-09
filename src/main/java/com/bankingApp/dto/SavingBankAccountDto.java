package com.bankingApp.dto;

import lombok.Data;

@Data
public class SavingBankAccountDto extends  BankAccountDto{
    private double interestRate;
}
