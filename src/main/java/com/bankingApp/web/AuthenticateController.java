package com.bankingApp.web;

import com.bankingApp.model.HttpResponse;
import com.bankingApp.model.JwtAuthRequest;
import com.bankingApp.model.JwtAuthResponse;
import com.bankingApp.service.impl.JwtService;
import com.bankingApp.utils.ResponseHandlerUtil;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/*
 Controller that exposes authenticate web service.
 */

@AllArgsConstructor

@RestController
@CrossOrigin
public class AuthenticateController {

     private JwtService jwtService;

    @PostMapping({"/authenticate"})
    public ResponseEntity<HttpResponse> authenticate(@RequestBody JwtAuthRequest jwtAuthRequest) throws BadCredentialsException {


            JwtAuthResponse jwtAuthResponse= jwtService.createJwtToken(jwtAuthRequest);

            return ResponseHandlerUtil.generateResponse("Successfully authenticated ",
                    HttpStatus.OK,
                    jwtAuthResponse);


    }
}
