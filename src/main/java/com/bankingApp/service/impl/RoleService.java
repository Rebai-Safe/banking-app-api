package com.bankingApp.service.impl;

import com.bankingApp.repository.RoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor

@Service
public class RoleService {

    private RoleRepository roleRepository;
}
