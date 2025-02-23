package com.modsen.auth.model;

import com.modsen.auth.security.user.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.Set;
import java.util.UUID;

import static com.modsen.auth.security.user.Role.ROLE_MEMBER;

/**
 * @author Alexander Dudkin
 */
public class User implements UserDetails, Serializable {

    private final UUID id;
    private final String username;
    private final String password;
    private final Set<Role> roles;

    public User(UUID id, String username, String password, Set<Role> roles) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.roles = roles != null ? roles : Set.of(ROLE_MEMBER);
    }

    public User withEncryptedPassword(String password) {
        return this.password.equals(password)
                ? this
                : new User(this.id, this.username, password, this.roles);
    }

    public User withRoles(Set<Role> roles) {
        return this.roles == roles
                ? this
                : new User(this.id, this.username, this.password, roles);
    }

    public UUID getId() {
        return id;
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
