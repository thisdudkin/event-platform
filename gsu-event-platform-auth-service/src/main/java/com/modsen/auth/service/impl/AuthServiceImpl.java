package com.modsen.auth.service.impl;

import com.modsen.auth.dto.request.AuthenticationRequest;
import com.modsen.auth.dto.request.RefreshTokenRequest;
import com.modsen.auth.dto.response.TokenResponse;
import com.modsen.auth.exception.BadCredentialsException;
import com.modsen.auth.exception.BadRefreshTokenException;
import com.modsen.auth.model.User;
import com.modsen.auth.repository.UserRepository;
import com.modsen.auth.security.provider.JwtTokenProvider;
import com.modsen.auth.security.utils.JwtUtils;
import com.modsen.auth.service.AuthService;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * @author Alexander Dudkin
 */
@Service
public class AuthServiceImpl implements AuthService {

    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    public AuthServiceImpl(JwtUtils jwtUtils, UserRepository userRepository, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider, AuthenticationManager authenticationManager) {
        this.jwtUtils = jwtUtils;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public TokenResponse authenticate(AuthenticationRequest request) {
        try {
            User user = userRepository.findByUsername(request.username())
                    .orElseThrow(() -> new AuthenticationCredentialsNotFoundException("Bad credentials"));
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    request.username(),
                    request.password()
            ));
            return jwtTokenProvider.generateTokens(user.getUsername(), user.getRoles());
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Bad credentials");
        }
    }

    @Override
    public TokenResponse refresh(RefreshTokenRequest request) {
        String refreshToken = request.refreshToken();
        if (!jwtUtils.isRefreshToken(refreshToken)) {
            throw new BadRefreshTokenException("Can't update tokens with non-refresh token");
        }

        String username = jwtUtils.getUsername(refreshToken);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new BadCredentialsException("Bad credentials"));
        return jwtTokenProvider.generateTokens(username, user.getRoles());
    }

}
