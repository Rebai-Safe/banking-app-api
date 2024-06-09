package com.bankingApp.service.impl;

import com.bankingApp.dto.*;
import com.bankingApp.entity.*;
import com.bankingApp.enums.AccountStatus;
import com.bankingApp.enums.OperationType;
import com.bankingApp.exception.BalanceNotSufficientException;
import com.bankingApp.exception.BankAccountNotFoundException;
import com.bankingApp.exception.CustomerNotFoundException;
import com.bankingApp.repository.AccountOperationRepository;
import com.bankingApp.repository.BankAccountRepository;
import com.bankingApp.repository.CustomerRepository;
import com.bankingApp.service.BankAccountService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@AllArgsConstructor

@Service
@Transactional
public class BankAccountServiceImpl implements BankAccountService {

    private CustomerRepository customerRepository;
    private BankAccountRepository bankAccountRepository;
    private AccountOperationRepository accountOperationRepository;
    private ModelMapper modelMapper;

    private static final Logger LOGGER = LoggerFactory.getLogger(BankAccountServiceImpl.class);



    @Override
    public CurrentBankAccountDto saveCurrentBankAccount(double initBalance, double overDraft, Long customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId).orElse(null);

        if (customer == null)
            throw new CustomerNotFoundException("Customer not found");

        CurrentAccount currentAccount = new CurrentAccount();

        currentAccount.setId(UUID.randomUUID().toString());
        currentAccount.setCreatedAt(new Date());
        currentAccount.setBalance(initBalance);
        currentAccount.setOverDraft(overDraft);
        currentAccount.setCustomer(customer);
        currentAccount.setStatus(AccountStatus.CREATED);

        CurrentAccount savedCurrentAccount = bankAccountRepository.save(currentAccount);


        return modelMapper.map(savedCurrentAccount, CurrentBankAccountDto.class);
    }

    @Override
    public SavingBankAccountDto saveSavingBankAccount(double initBalance, double interestRate, Long customerId) throws CustomerNotFoundException {

        Customer customer = customerRepository.findById(customerId).orElse(null);

        if (customer == null)
            throw new CustomerNotFoundException("Customer not found");

        SavingAccount savingAccount = new SavingAccount();

        savingAccount.setId(UUID.randomUUID().toString());
        savingAccount.setCreatedAt(new Date());
        savingAccount.setBalance(initBalance);
        savingAccount.setCustomer(customer);
        savingAccount.setInterestRate(interestRate);
        savingAccount.setStatus(AccountStatus.CREATED);

        SavingAccount savedSavingAccount = bankAccountRepository.save(savingAccount);


        return modelMapper.map(savedSavingAccount, SavingBankAccountDto.class);
    }



    @Override
    public BankAccountDto getBankAccount(String accountId) throws BankAccountNotFoundException {
        BankAccount bankAccount = bankAccountRepository.findById(accountId)
                .orElseThrow(() -> new BankAccountNotFoundException("Account not found"));

        CustomerDto customerDto = modelMapper.map(bankAccount.getCustomer(), CustomerDto.class);

        BankAccountDto bankAccountDto;

        if(bankAccount instanceof CurrentAccount){
             bankAccountDto = modelMapper.map(bankAccount, CurrentBankAccountDto.class);
        } else{
             bankAccountDto = modelMapper.map(bankAccount, SavingBankAccountDto.class);
        }

        bankAccountDto.setCustomerDto(customerDto);

        return bankAccountDto;
    }

    @Override
    public void debit(String accountId, double amount, String description) throws BankAccountNotFoundException, BalanceNotSufficientException {
        BankAccount bankAccount = bankAccountRepository.findById(accountId)
                .orElseThrow(() -> new BankAccountNotFoundException("Account not found"));

        if(bankAccount.getBalance() < amount){
            throw new BalanceNotSufficientException("Balance not sufficient");
        }


        AccountOperation accountOperation = new AccountOperation();
        accountOperation.setOperationType(OperationType.DEBIT);
        accountOperation.setAmount(amount);
        accountOperation.setDescription(description);
        accountOperation.setOperationDate(new Date());
        accountOperation.setBankAccount(bankAccount);

        accountOperationRepository.save(accountOperation);

        bankAccount.setBalance(bankAccount.getBalance() - amount);
        bankAccountRepository.save(bankAccount);

    }

    @Override
    public void credit(String accountId, double amount, String description) throws BankAccountNotFoundException {
        BankAccount bankAccount = bankAccountRepository.findById(accountId)
                .orElseThrow(() -> new BankAccountNotFoundException("Account not found"));


        AccountOperation accountOperation = new AccountOperation();
        accountOperation.setOperationType(OperationType.CREDIT);
        accountOperation.setAmount(amount);
        accountOperation.setDescription(description);
        accountOperation.setOperationDate(new Date());
        accountOperation.setBankAccount(bankAccount);

        accountOperationRepository.save(accountOperation);

        bankAccount.setBalance(bankAccount.getBalance() + amount);
        bankAccountRepository.save(bankAccount);
    }

    @Override
    public void transfer(String accountIdSource, String accountIdDestination, double amount) throws BankAccountNotFoundException, BalanceNotSufficientException {
        debit(accountIdSource, amount, "Transfer to "+accountIdDestination);
        credit(accountIdDestination, amount, "Transfer from "+accountIdSource);
    }

    @Override
    public List<BankAccountDto> getBankAccountList() {

        List<BankAccount> bankAccounts = bankAccountRepository.findAll();

        List<CustomerDto> customerDtos = bankAccounts.stream().map(bankAccount -> modelMapper.map(bankAccount.getCustomer(),
                CustomerDto.class)).collect(Collectors.toList());

        List<BankAccountDto> bankAccountDtos = bankAccountRepository.findAll().stream()
                .map(bankAccount -> {
                     if(bankAccount instanceof CurrentAccount)
                      {

                          CurrentBankAccountDto currentBankAccountDto = modelMapper.map(bankAccount, CurrentBankAccountDto.class);
                          currentBankAccountDto.setType(bankAccount.getClass().getSimpleName());
                          return currentBankAccountDto;
                      }

                     else {
                         SavingBankAccountDto savingBankAccountDto = modelMapper.map(bankAccount, SavingBankAccountDto.class);
                         savingBankAccountDto.setType(bankAccount.getClass().getSimpleName());
                         return savingBankAccountDto;
                     }


                }).collect(Collectors.toList());

        for(int i = 0 ; i<bankAccountDtos.size(); i++){
            bankAccountDtos.get(i).setCustomerDto(customerDtos.get(i));
        }

        return bankAccountDtos;
   }


    @Override
    public List<AccountOperationDto> accountHistory(String accountId){
        return accountOperationRepository.findByBankAccount_Id(accountId).stream().map(operation ->
                modelMapper.map(operation, AccountOperationDto.class)).collect(Collectors.toList());
    }

    /**
     * returns account info & page of operations history
     * @param accountId account id
     * @param page current page
     * @param size total size
     * @return AccountHistoryDto
     */
    @Override
    public AccountHistoryDto getAccountHistoryPage(String accountId, int page, int size) throws BankAccountNotFoundException {

        BankAccount bankAccount = bankAccountRepository.findById(accountId).orElse(null);

        if(bankAccount == null){
            throw new BankAccountNotFoundException("bank account not found");
        }
        Page<AccountOperation> accountOperationsPage = accountOperationRepository.findByBankAccount_IdOrderByOperationDateDesc(accountId, PageRequest.of(page, size));

        AccountHistoryDto accountHistoryDto = new AccountHistoryDto();

        accountHistoryDto.setBalance(bankAccount.getBalance());
        accountHistoryDto.setCurrentPage(page);
        accountHistoryDto.setPageSize(size);
        accountHistoryDto.setAccountId(accountId);
        accountHistoryDto.setTotalPages(accountOperationsPage.getTotalPages());
        accountHistoryDto.setAccountOperationDtoList(accountOperationsPage.getContent().stream().map(accountOperation ->
                 modelMapper.map(accountOperation, AccountOperationDto.class)).collect(Collectors.toList()));

        return accountHistoryDto;
    }

}
