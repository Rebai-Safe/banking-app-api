package com.bankingApp.config;

import com.bankingApp.filter.JwtRequestFilter;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;

/*
 In a Spring Security application, various filters are responsible for performing different security-related tasks,
 such as authentication, authorization, and request processing.
 These filters are organized into a chain, and each filter has a specific role in handling incoming HTTP requests.
 The order in which filters are applied is crucial for the overall security of the application
 */
@AllArgsConstructor

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private JwtRequestFilter jwtRequestFilter;


    /* ----spring security 6 configuration----
       HttpSecurity is a class that allows to define how incoming HTTP requests should be secured,
       which resources should be protected, and how different aspects of security,
       such as authentication and authorization, should be handled
    */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests((requests) ->{
                    requests.requestMatchers("/authenticate")
                            .permitAll()
                            //.requestMatchers("/user")
                            // .hasAuthority("USER")
                            //.requestMatchers("/admin")
                            //.hasAuthority("ADMIN")
                            .anyRequest()
                            .authenticated();
                });

        //use the default configuration settings for HTTP Basic Authentication
        http.httpBasic(withDefaults());

        /*
          The UsernamePasswordAuthenticationFilter is a built-in Spring Security filter
          responsible for handling HTTP Basic and form-based authentication
          By adding a custom filter before it,
          you can perform custom authentication or authorization logic
          before Spring Security's default authentication mechanism.
         */
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        /* ---- session management ----
           SessionCreationPolicy.STATELESS:
           This policy means that your application will not create or use HTTP sessions
           to store user authentication information or any other session-related data.
           In a stateless configuration, each HTTP request is treated independently,
           and the server does not store any session state between requests.
           This is commonly used in RESTful web services and APIs where the client sends
           all required information with each request (e.g., using tokens or credentials)
           and there's no need to maintain server-side session state
         */
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.cors();
       return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }


}
