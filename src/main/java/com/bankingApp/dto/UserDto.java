package com.bankingApp.dto;

import lombok.*;

import java.util.Set;

@Data
public class UserDto {
    private String firstName;
    private String lastName;
    private Set<RoleDto> roles;
}
