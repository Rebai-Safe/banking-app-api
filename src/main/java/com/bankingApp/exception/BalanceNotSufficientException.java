package com.bankingApp.exception;

public class BalanceNotSufficientException extends Exception {
    public BalanceNotSufficientException(String message) {
         super(message);

    }
}
