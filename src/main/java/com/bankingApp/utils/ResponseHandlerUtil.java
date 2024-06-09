package com.bankingApp.utils;

import com.bankingApp.model.HttpResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

/*
 a handler class that allows to formulate http response,
 To be used by all endpoints to return their response.
 */
public class ResponseHandlerUtil {

    public static ResponseEntity<HttpResponse> generateResponse(String message, HttpStatus status, Object object){
          HttpResponse response = new HttpResponse(message, status.value(), object);

          return new ResponseEntity<>(response, status);

    }
}
