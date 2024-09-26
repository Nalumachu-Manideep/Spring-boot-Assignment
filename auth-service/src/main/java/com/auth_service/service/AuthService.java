package com.auth_service.service;

import com.auth_service.entity.UserCredential;

public interface AuthService {
    public String saveUser(UserCredential credential);
    public String generateToken(String username);
    public void validateToken(String token);

}
