package com.modsen.auth.controller;

import com.modsen.auth.controller.api.AuthApi;
import com.modsen.auth.dto.request.AuthenticationRequest;
import com.modsen.auth.dto.request.RefreshTokenRequest;
import com.modsen.auth.dto.request.RegistrationFields;
import com.modsen.auth.dto.response.TokenResponse;
import com.modsen.auth.service.AuthService;
import com.modsen.auth.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Alexander Dudkin
 */
@RestController
@RequestMapping(AuthApi.URI)
public class AuthController implements AuthApi {

    private final UserService userService;
    private final AuthService authService;

    public AuthController(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody RegistrationFields request) {
        userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> authenticate(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(authService.authenticate(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refreshTokens(@RequestBody RefreshTokenRequest request) {
        return ResponseEntity.ok(authService.refresh(request));
    }

}
