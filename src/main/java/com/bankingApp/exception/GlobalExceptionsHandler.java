package com.bankingApp.exception;


import com.bankingApp.model.HttpResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Exceptions handler
 */

@ControllerAdvice
public class GlobalExceptionsHandler {

    @ExceptionHandler(value = CustomerNotFoundException.class)
    public ResponseEntity<HttpResponse> handleCustomerNotFoundException(CustomerNotFoundException exception){
        HttpResponse httpResponse = new HttpResponse(
                "Customer not found",
                HttpStatus.NOT_FOUND.value(),
                exception.getMessage()
                );

        return new ResponseEntity<>(httpResponse, HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(value = BankAccountNotFoundException.class)
    public ResponseEntity<HttpResponse> handleBankAccountNotFoundException(BankAccountNotFoundException exception){
        HttpResponse httpResponse = new HttpResponse(
                "Requested bank account not found",
                HttpStatus.NOT_FOUND.value(),
                exception.getMessage()
                );

        return  new ResponseEntity<>(httpResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = BalanceNotSufficientException.class)
    public ResponseEntity<HttpResponse> handleBalanceNotSufficientException(BalanceNotSufficientException exception){
        HttpResponse httpResponse = new HttpResponse(
                "Balance not sufficient",
                HttpStatus.BAD_REQUEST.value(),
                exception.getMessage()
        );

        return  new ResponseEntity<>(httpResponse, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(value = BadCredentialsException.class)
    public ResponseEntity<HttpResponse> handleBadCredentialsException(BadCredentialsException exception){
        HttpResponse httpResponse = new HttpResponse(
                "username / password is incorrect",
                         HttpStatus.BAD_REQUEST.value(),
                         exception.getMessage());

        return  new ResponseEntity<>(httpResponse, HttpStatus.BAD_REQUEST);

    }

}
