package com.bankingApp.service;

import com.bankingApp.dto.*;
import com.bankingApp.exception.BalanceNotSufficientException;
import com.bankingApp.exception.BankAccountNotFoundException;
import com.bankingApp.exception.CustomerNotFoundException;

import java.util.List;

public interface BankAccountService {


    CurrentBankAccountDto saveCurrentBankAccount(double initBalance, double overDraft, Long customerId) throws CustomerNotFoundException;
    SavingBankAccountDto saveSavingBankAccount(double initBalance, double interestRate, Long customerId) throws CustomerNotFoundException;

    BankAccountDto getBankAccount(String accountId) throws BankAccountNotFoundException;
    void debit(String accountId, double amount, String description) throws BankAccountNotFoundException, BalanceNotSufficientException;
    void credit(String accountId, double amount, String description) throws BankAccountNotFoundException;
    void transfer(String accountIdSource,String accountIdDestination, double amount) throws BankAccountNotFoundException, BalanceNotSufficientException;
    List<BankAccountDto> getBankAccountList();
    List<AccountOperationDto> accountHistory(String accountId);

    AccountHistoryDto getAccountHistoryPage(String accountId, int page, int size) throws BankAccountNotFoundException;
}
