package com.bankingApp.repository;

import com.bankingApp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {


    public User findByUserName(String userName);
}
