package com.modsen.auth.security.provider;

import com.modsen.auth.dto.response.TokenResponse;
import com.modsen.auth.security.user.JwtProperties;
import com.modsen.auth.security.user.Role;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

/**
 * @author Alexander Dudkin
 */
@Component
public class JwtTokenProvider {

    private final Key key;
    private final JwtProperties jwtProperties;

    public JwtTokenProvider(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
        this.key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(jwtProperties.secretKey().getBytes()));
    }

    public TokenResponse generateTokens(String username, Set<Role> roles) {
        return new TokenResponse(
                createToken(username, roles, jwtProperties.getAccessExpiration(), JwtProperties.ACCESS_PREFIX),
                jwtProperties.getAccessExpiration(),
                createToken(username, Collections.emptySet(), jwtProperties.getRefreshExpiration(), JwtProperties.REFRESH_PREFIX),
                jwtProperties.getRefreshExpiration()
        );
    }

    private String createToken(String username, Set<Role> roles, Long tokenExpiration, String tokenType) {
        Date now = new Date();
        Date lifetime = new Date(now.getTime() + tokenExpiration);

        return Jwts.builder()
                .subject(username)
                .issuedAt(now)
                .expiration(lifetime)
                .signWith(key)
                .claim(JwtProperties.AUTH_CLAIM, roles.stream()
                        .map(role -> new SimpleGrantedAuthority(role.getAuthority()))
                        .toList())
                .claim(JwtProperties.TOKEN_TYPE, tokenType)
                .compact();
    }

}
