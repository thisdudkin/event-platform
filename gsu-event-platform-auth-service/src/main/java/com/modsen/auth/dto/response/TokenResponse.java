package com.modsen.auth.dto.response;

import java.io.Serializable;

/**
 * @author Alexander Dudkin
 */
public record TokenResponse(
        String accessToken,
        long accessExpiration,
        String refreshToken,
        long refreshExpiration
) implements Serializable { }
