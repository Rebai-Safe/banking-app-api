package com.bankingApp.web;

import com.bankingApp.dto.*;
import com.bankingApp.exception.BalanceNotSufficientException;
import com.bankingApp.exception.BankAccountNotFoundException;
import com.bankingApp.model.HttpResponse;
import com.bankingApp.service.BankAccountService;
import com.bankingApp.utils.ResponseHandlerUtil;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor

@RestController
@RequestMapping("/accounts")
public class BankAccountController {

    private BankAccountService bankAccountService;


    @GetMapping("{id}")
    public ResponseEntity<HttpResponse> getBankAccount(@PathVariable("id") String accountId) throws BankAccountNotFoundException {

        return ResponseHandlerUtil.generateResponse("Bank account returned successfully",
                HttpStatus.OK,
                bankAccountService.getBankAccount(accountId));
    }

    @GetMapping("/")
    public ResponseEntity<HttpResponse> getAccountsList(){

        return ResponseHandlerUtil.generateResponse("Accounts list returned successfully",
                HttpStatus.OK,
                bankAccountService.getBankAccountList());
    }

    @GetMapping("{id}/operations")
    public ResponseEntity<HttpResponse> getAccountHistory(@PathVariable("id") String accountId){

        return ResponseHandlerUtil.generateResponse("Account Operations list returned successfully",
                HttpStatus.OK,
                bankAccountService.accountHistory(accountId));
    }

    @GetMapping("{id}/PageOperations")
    public ResponseEntity<HttpResponse> getAccountHistoryPage(
            @PathVariable("id") String accountId,
            @RequestParam(name="page", defaultValue = "0") int page,
            @RequestParam(name="size", defaultValue = "5")int size) throws BankAccountNotFoundException {

          return ResponseHandlerUtil.generateResponse("Account Operations list returned successfully",
                HttpStatus.OK,
                bankAccountService.getAccountHistoryPage(accountId, page, size));
    }

    @PostMapping("debit")
    public  ResponseEntity<HttpResponse> debit(@RequestBody DebitDTO debitDTO) throws BankAccountNotFoundException, BalanceNotSufficientException {
        this.bankAccountService.debit(debitDTO.getAccountId(),debitDTO.getAmount(),debitDTO.getDescription());

        return ResponseHandlerUtil.generateResponse("Debit done successfully",
                HttpStatus.OK,
                debitDTO);
    }
    @PostMapping("credit")
    public ResponseEntity<HttpResponse> credit(@RequestBody CreditDTO creditDTO) throws BankAccountNotFoundException {
        this.bankAccountService.credit(creditDTO.getAccountId(),creditDTO.getAmount(),creditDTO.getDescription());

        return ResponseHandlerUtil.generateResponse("Debit done successfully",
                HttpStatus.OK,
                creditDTO);
    }
    @PostMapping("transfer")
    public ResponseEntity<HttpResponse> transfer(@RequestBody TransferRequestDTO transferRequestDTO) throws BankAccountNotFoundException, BalanceNotSufficientException {
        this.bankAccountService.transfer(
                transferRequestDTO.getAccountSource(),
                transferRequestDTO.getAccountDestination(),
                transferRequestDTO.getAmount());

        return ResponseHandlerUtil.generateResponse("Transfer done successfully",
                HttpStatus.OK,
                "");
    }

}
