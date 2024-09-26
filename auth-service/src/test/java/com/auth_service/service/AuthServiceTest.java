package com.auth_service.service;



import com.auth_service.entity.UserCredential;
import com.auth_service.repository.UserCredentialRepository;
import com.auth_service.util.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private UserCredentialRepository userCredentialRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthServiceImpl authService;

    private UserCredential userCredential;

    @BeforeEach
    public void setUp() {
        userCredential = new UserCredential();
        userCredential.setName("testUser");
        userCredential.setPassword("testPassword");
    }

    @Test
    public void saveUser_ShouldEncodePasswordAndSaveUser() {

        when(passwordEncoder.encode("testPassword")).thenReturn("encodedPassword");

        String response = authService.saveUser(userCredential);

        assertEquals("user added to the system", response);
        verify(passwordEncoder, times(1)).encode("testPassword");
        verify(userCredentialRepository, times(1)).save(userCredential);
        assertEquals("encodedPassword", userCredential.getPassword()); // Now it checks the encoded password
    }


    @Test
    public void generateToken_ShouldReturnToken() {
        String expectedToken = "token123";
        when(jwtService.generateToken("testUser")).thenReturn(expectedToken);

        String token = authService.generateToken("testUser");

        assertEquals(expectedToken, token);
        verify(jwtService, times(1)).generateToken("testUser");
    }

    @Test
    public void validateToken_ShouldCallJwtServiceValidateToken() {
        String token = "validToken";
        doNothing().when(jwtService).validateToken(token);

        authService.validateToken(token);

        verify(jwtService, times(1)).validateToken(token);
    }
}

