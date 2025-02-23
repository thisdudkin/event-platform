package com.modsen.auth.security.user;

import org.springframework.security.core.GrantedAuthority;

/**
 * @author Alexander Dudkin
 */
public enum Role implements GrantedAuthority {
    ROLE_ADMIN, ROLE_MEMBER;

    @Override
    public String getAuthority() {
        return name();
    }

}
