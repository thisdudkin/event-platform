package com.modsen.auth.security.user;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.Serializable;
import java.util.Map;

/**
 * @author Alexander Dudkin
 */
@ConfigurationProperties(prefix = JwtProperties.PREFIX)
public record JwtProperties(
        String secretKey,
        Map<String, Long> expiration
) implements Serializable {

    public static final String PREFIX = "jwt";
    public static final String AUTH_CLAIM = "auth";
    public static final String BEARER_PREFIX = "Bearer ";
    public static final String TOKEN_TYPE = "token_type";
    public static final String ACCESS_PREFIX = "access";
    public static final String REFRESH_PREFIX = "refresh";

    public Long getAccessExpiration() {
        return expiration.getOrDefault("access", 50000L);
    }

    public Long getRefreshExpiration() {
        return expiration.getOrDefault("refresh", 600000L);
    }

}
