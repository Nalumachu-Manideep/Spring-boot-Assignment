package com.auth_service.controller;

import com.auth_service.dto.AuthRequest;
import com.auth_service.entity.UserCredential;
import com.auth_service.exception.InvalidCredentialsException;
import com.auth_service.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    @Mock
    private AuthService authService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private AuthController authController;

    private UserCredential userCredential;
    private AuthRequest authRequest;

    @BeforeEach
    public void setUp() {
        userCredential = new UserCredential();
        userCredential.setName("testUser");
        userCredential.setPassword("testPassword");

        authRequest = new AuthRequest();
        authRequest.setUsername("testUser");
        authRequest.setPassword("testPassword");
    }

    @Test
    public void addNewUser_ShouldReturnSuccessMessage() {
        when(authService.saveUser(any(UserCredential.class))).thenReturn("User registered successfully");

        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(false);

        String result = authController.addNewUser(userCredential, bindingResult);

        assertEquals("User registered successfully", result);
        verify(authService, times(1)).saveUser(userCredential);
    }

    @Test
    public void addNewUser_InvalidUser_ShouldThrowInvalidCredentialsException() {
        UserCredential invalidUser = new UserCredential();
        invalidUser.setName(""); // Invalid because name is empty
        invalidUser.setPassword("test"); // Assuming password must be longer

        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(true);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(
                new FieldError("userCredential", "name", "Name must not be empty"),
                new FieldError("userCredential", "password", "Password must be at least 6 characters long")
        ));

        InvalidCredentialsException exception = assertThrows(InvalidCredentialsException.class, () -> {
            authController.addNewUser(invalidUser, bindingResult);
        });

        assertEquals("Validation errors:---> name-->  Name must not be empty;   password-->  Password must be at least 6 characters long;   ", exception.getMessage());
        verify(authService, never()).saveUser(any(UserCredential.class));
    }

    @Test
    public void getToken_ValidCredentials_ShouldReturnToken() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authService.generateToken("testUser")).thenReturn("token123");

        String token = authController.getToken(authRequest);

        assertEquals("token123", token);
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(authService, times(1)).generateToken("testUser");
    }

    @Test
    public void getToken_InvalidCredentials_ShouldThrowInvalidCredentialsException() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenThrow(new RuntimeException("Authentication failed"));

        InvalidCredentialsException exception = assertThrows(InvalidCredentialsException.class, () -> authController.getToken(authRequest));

        assertEquals("Authentication failed: Authentication failed", exception.getMessage());
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(authService, never()).generateToken(anyString());
    }

    @Test
    public void validateToken_ValidToken_ShouldReturnSuccessMessage() {
        String token = "validToken";
        doNothing().when(authService).validateToken(token);

        String result = authController.validateToken(token);

        assertEquals("Token is valid", result);
        verify(authService, times(1)).validateToken(token);
    }

    @Test
    public void validateToken_InvalidToken_ShouldThrowException() {
        String token = "invalidToken";
        doThrow(new RuntimeException("Invalid token")).when(authService).validateToken(token);

        Exception exception = assertThrows(RuntimeException.class, () -> authController.validateToken(token));

        assertEquals("Invalid token", exception.getMessage());
        verify(authService, times(1)).validateToken(token);
    }
}
