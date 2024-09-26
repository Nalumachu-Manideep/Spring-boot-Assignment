package com.auth_service.controller;

import com.auth_service.dto.AuthRequest;
import com.auth_service.entity.UserCredential;
import com.auth_service.exception.InvalidCredentialsException;
import com.auth_service.service.AuthService;
import com.auth_service.util.Constants;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(Constants.AUTH_BASE_URL)
@RequiredArgsConstructor
public class AuthController {
    private final AuthService service;

    private final AuthenticationManager authenticationManager;


    @PostMapping(Constants.REGISTER_URL)
    public String addNewUser(@Valid @RequestBody UserCredential userCredential, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder("Validation errors:---> ");
            bindingResult.getFieldErrors().forEach(error ->
                    errorMessage.append(error.getField()).append("-->  ").append(error.getDefaultMessage()).append(";   ")
            );
            throw new InvalidCredentialsException(errorMessage.toString());
        }
        return service.saveUser(userCredential);
    }


    @PostMapping(Constants.TOKEN_URL)
    public String getToken(@RequestBody AuthRequest authRequest) {
        if (authRequest == null || authRequest.getUsername() == null || authRequest.getPassword() == null) {
            throw new InvalidCredentialsException("Authentication failed: null");
        }
        String token = "";
        try {
            Authentication authenticate = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
            );
            if (authenticate.isAuthenticated()) {
                token = service.generateToken(authRequest.getUsername());
            }
       }catch (Exception e) {
            throw new InvalidCredentialsException("Authentication failed: " + e.getMessage());
        }
        return token;
    }


    @GetMapping(Constants.VALIDATE_URL)
    public String validateToken(@RequestParam("token") String token) {
        service.validateToken(token);
        return "Token is valid";
    }
}
