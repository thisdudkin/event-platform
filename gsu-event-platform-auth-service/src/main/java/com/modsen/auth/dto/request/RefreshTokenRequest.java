package com.modsen.auth.dto.request;

import java.io.Serializable;

/**
 * @author Alexander Dudkin
 */
public record RefreshTokenRequest(
        String refreshToken
) implements Serializable { }
