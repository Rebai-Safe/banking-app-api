package com.bankingApp.config;

import com.bankingApp.dto.BankAccountDto;
import com.bankingApp.dto.CustomerDto;
import com.bankingApp.entity.*;
import com.bankingApp.enums.AccountStatus;
import com.bankingApp.enums.OperationType;
import com.bankingApp.exception.BalanceNotSufficientException;
import com.bankingApp.exception.BankAccountNotFoundException;
import com.bankingApp.exception.CustomerNotFoundException;
import com.bankingApp.repository.AccountOperationRepository;
import com.bankingApp.repository.BankAccountRepository;
import com.bankingApp.repository.CustomerRepository;
import com.bankingApp.repository.UserRepository;
import com.bankingApp.service.BankAccountService;
import com.bankingApp.service.CustomerService;
import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@Configuration
public class AppConfig {

    // Authentication Manager will choose the right authentication provider.
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    //create model mapper instance and register it as a bean
    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }



    //run at the start of the application

    //@Bean
    CommandLineRunner commandLineRunner(BankAccountService bankAccountService,
                                        CustomerService customerService,
                                        UserRepository userRepository){

        return  args -> {
            Stream.of("safe", "chayma").forEach( nameU -> {
                User user = new User();
                user.setUserName(nameU);
                user.setPassword("password");
                userRepository.save(user);
            });
            Stream.of("Hassan", "yassine", "aicha").forEach(
                    name -> {
                        Customer customer = new Customer();
                        customer.setName(name);
                        customer.setEmail(name+"@hotmail.com");
                        customerService.saveCustomer(modelMapper().map(customer, CustomerDto.class));
                    });

            customerService.listCustomer().forEach(customerDto -> {

                try {
                    bankAccountService.saveCurrentBankAccount(Math.random()*9000,
                            9000,customerDto.getId());
                    bankAccountService.saveSavingBankAccount(Math.random()*12000,
                            5.5, customerDto.getId());

                 } catch (CustomerNotFoundException e) {
                    e.printStackTrace();
                }

            });
            List<BankAccountDto> bankAccountDtos = bankAccountService.getBankAccountList();

            for(BankAccountDto bankAccountDto: bankAccountDtos){
                for (int i = 0; i < 10; i++) {
                    bankAccountService.credit(bankAccountDto.getId(), 10000*Math.random()*120000,
                            "Credit");
                    bankAccountService.debit(bankAccountDto.getId(), 1000+Math.random()*9000,
                            "Debit");
                }
            }

        };
    }


    //@Bean
    CommandLineRunner start(CustomerRepository customerRepository,
                            BankAccountRepository bankAccountRepository,
                            AccountOperationRepository accountOperationRepository,
                            BankAccountService bankAccountService){
        return args -> {
            //test customer creation
            Stream.of("Hassan", "yassine", "aicha").forEach(
                    name -> {
                        Customer customer = new Customer();
                        customer.setName(name);
                        customer.setEmail(name+"@hotmail.com");
                        customerRepository.save(customer);
                    });

            //test account creation
            customerRepository.findAll().forEach(customer ->{
                CurrentAccount currentAccount = new CurrentAccount();
                currentAccount.setId(UUID.randomUUID().toString());
                currentAccount.setBalance(Math.random()*9000);
                currentAccount.setCreatedAt(new Date());
                currentAccount.setStatus(AccountStatus.CREATED);
                currentAccount.setCustomer(customer);
                currentAccount.setOverDraft(9000);
                bankAccountRepository.save(currentAccount);

                SavingAccount savingAccount = new SavingAccount();
                savingAccount.setId(UUID.randomUUID().toString());
                savingAccount.setBalance(Math.random()*9000);
                savingAccount.setCreatedAt(new Date());
                savingAccount.setStatus(AccountStatus.CREATED);
                savingAccount.setCustomer(customer);
                savingAccount.setInterestRate(5.5);
                bankAccountRepository.save(savingAccount);
            });

            //test operation creation
            bankAccountRepository.findAll().forEach(
                    account -> {
                        for(int i = 0; i < 5 ; i++){
                            AccountOperation accountOperation = new AccountOperation();
                            accountOperation.setOperationDate(new Date());
                            accountOperation.setAmount(Math.random()*12000);
                            accountOperation.setOperationType(Math.random()>0.5 ? OperationType.DEBIT : OperationType.CREDIT);
                            accountOperation.setBankAccount(account);
                            accountOperationRepository.save(accountOperation);
                        }
                    }
            );

        };
    }
}
