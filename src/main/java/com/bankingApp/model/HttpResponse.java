package com.bankingApp.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor

@Data
public class HttpResponse {
    private String message;
    private int  status;
    private Object object;
}
