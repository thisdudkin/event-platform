package com.modsen.auth.service;

import com.modsen.auth.dto.request.AuthenticationRequest;
import com.modsen.auth.dto.request.RefreshTokenRequest;
import com.modsen.auth.dto.response.TokenResponse;

/**
 * @author Alexander Dudkin
 */
public interface AuthService {
    TokenResponse authenticate(AuthenticationRequest request);
    TokenResponse refresh(RefreshTokenRequest request);
}
