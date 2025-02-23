package com.modsen.auth.security.utils;

import com.modsen.auth.exception.CustomJwtException;
import com.modsen.auth.security.user.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Base64;

/**
 * @author Alexander Dudkin
 */
@Component
public class JwtUtils {

    private final Key key;
    private final JwtProperties jwtProperties;
    private final UserDetailsService customUserDetails;

    public JwtUtils(JwtProperties jwtProperties, UserDetailsService customUserDetails) {
        this.jwtProperties = jwtProperties;
        this.customUserDetails = customUserDetails;
        this.key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(jwtProperties.secretKey().getBytes()));
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = customUserDetails.loadUserByUsername(getUsername(token));
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (bearerToken != null && bearerToken.startsWith(JwtProperties.BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith((SecretKey) key).build()
                    .parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            throw new CustomJwtException("Expired or invalid JWT token", HttpStatus.UNAUTHORIZED);
        }
    }

    public boolean isRefreshToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith((SecretKey) key).build()
                    .parseSignedClaims(token)
                    .getPayload();
            String tokenType = claims.get(JwtProperties.TOKEN_TYPE, String.class);
            return JwtProperties.REFRESH_PREFIX.equals(tokenType);
        } catch (JwtException | IllegalArgumentException e) {
            throw new CustomJwtException("Expired or invalid JWT token", HttpStatus.UNAUTHORIZED);
        }
    }

    public String getUsername(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) key).build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

}
