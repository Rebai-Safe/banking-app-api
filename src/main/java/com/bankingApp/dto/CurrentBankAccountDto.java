package com.bankingApp.dto;

import lombok.Data;

@Data
public class CurrentBankAccountDto extends BankAccountDto{
    private double overDraft;
}
