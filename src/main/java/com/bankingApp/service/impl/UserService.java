package com.bankingApp.service.impl;

import com.bankingApp.entity.User;
import com.bankingApp.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor

@Service
public class UserService {

    private UserRepository userRepository;

    public User findUserByUserName(String userName){
        return userRepository.findByUserName(userName);
    }
}
