package com.bankingApp.filter;

import com.bankingApp.service.impl.JwtService;
import com.bankingApp.utils.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@AllArgsConstructor

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private JwtUtil jwtUtil;
    private JwtService jwtService;
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtRequestFilter.class);


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {



        final String header = request.getHeader("Authorization");


        String jwtToken;
        String userName;

        if(header != null && header.startsWith("Bearer")){
            jwtToken = header.substring(7);

            try{
                userName = jwtUtil.getUserNameFromToken(jwtToken);

                if(userName != null && SecurityContextHolder.getContext().getAuthentication() == null){
                    UserDetails userDetails = jwtService.loadUserByUsername(userName);

                     if(jwtUtil.validateToken(jwtToken, userDetails)){
                         UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                                 new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                         usernamePasswordAuthenticationToken
                                 .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));



                         SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

                     }

                }
            } catch (IllegalArgumentException e){
                LOGGER.error("Unable to get JWT token");
            } catch (ExpiredJwtException e){
                //TODO handle this exception
                LOGGER.error("Jwt token is expired");
            }
        } else {
            LOGGER.info("Jwt token does not start with bearer");
        }


        filterChain.doFilter(request, response);

      }
}
