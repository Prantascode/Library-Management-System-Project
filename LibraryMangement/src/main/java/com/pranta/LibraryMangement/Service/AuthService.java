package com.pranta.LibraryMangement.Service;




import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.pranta.LibraryMangement.DTOs.DTO.LogicResponseDto;
import com.pranta.LibraryMangement.DTOs.DTO.LoginRequestDto;
import com.pranta.LibraryMangement.Entity.User;


@Service
public class AuthService {
    
    private final AuthenticationManager authenticationManager;
    private final UserService userService;

    public AuthService(AuthenticationManager authenticationManager, UserService userService){
        this.authenticationManager = authenticationManager;
        this.userService = userService;
    }

    public LogicResponseDto login(LoginRequestDto loginRequest){
        try{
           Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest .getEmail(),
                     loginRequest.getPassword()
                )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            User user = userService.findUserByEmail(loginRequest.getEmail());

            String token = "auth_" +System.currentTimeMillis() + "_" + user.getId();

            return new LogicResponseDto(
                user.getEmail(),
                user.getName(),
                user.getRole(),
                token
            );
        }catch (BadCredentialsException e){
            throw new AuthenticationServiceException("Invalid email or password");
        }
    }
}
