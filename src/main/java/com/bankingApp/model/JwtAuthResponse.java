package com.bankingApp.model;

import com.bankingApp.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

@AllArgsConstructor

public class JwtAuthResponse {

    private UserDto user;
    private String jwtToken;
}
