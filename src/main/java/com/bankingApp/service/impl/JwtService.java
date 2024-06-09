package com.bankingApp.service.impl;

import com.bankingApp.dto.UserDto;
import com.bankingApp.entity.User;
import com.bankingApp.model.JwtAuthRequest;
import com.bankingApp.model.JwtAuthResponse;

import com.bankingApp.utils.JwtUtil;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;


//this service exposes authentication functionalities
@Service
public class JwtService implements UserDetailsService {

    private UserService userService;
    private ModelMapper modelMapper;
    private JwtUtil jwtUtil;

    private AuthenticationManager authenticationManager;

    public JwtService(UserService userService, ModelMapper modelMapper, JwtUtil jwtUtil) {
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.jwtUtil = jwtUtil;
    }

    //use setter injection && @lazy here to avoid circular dependencies
    @Autowired
    public void setAuthenticationManager(@Lazy AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    //implementing loadUserByName method
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userService.findUserByUserName(username);

        if (user != null) {
            return new org.springframework.security.core.userdetails.User(user.getUserName(),
                    user.getPassword(),
                    getAuthorities(user));
        } else {
            throw new UsernameNotFoundException("User name is invalid");
        }
    }


    //authenticates user && creates a jwt token based on user details
    public JwtAuthResponse createJwtToken(JwtAuthRequest jwtAuthRequest) throws BadCredentialsException {
        String userName = jwtAuthRequest.getUserName();
        String password = jwtAuthRequest.getUserPassword();


        authenticate(userName, password);

        UserDetails userDetails = loadUserByUsername(userName);

        String newGeneratedToken = jwtUtil.generateToken(userDetails);

        User user = userService.findUserByUserName(userName);

        return new JwtAuthResponse(modelMapper.map(user, UserDto.class), newGeneratedToken);


    }

    //to handle exceptions
    private void authenticate(String username, String userPassword) throws BadCredentialsException {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, userPassword));
    }

    //transforms user roles to a set of SimpleGrantedAuthority, to be used in user Details object
    private Set<SimpleGrantedAuthority> getAuthorities(User user) {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();

        user.getRoles().forEach(role ->
                authorities.add(new SimpleGrantedAuthority(role.getRole())));


        return authorities;

    }


}
