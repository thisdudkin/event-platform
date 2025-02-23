package com.modsen.auth.security.user;

import com.modsen.auth.exception.UserNotFoundException;
import com.modsen.auth.model.User;
import com.modsen.auth.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author Alexander Dudkin
 */
@Component
public class CustomUserDetails implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetails(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername(username);

        if (user.isEmpty()) {
            throw new UserNotFoundException("No user found with username: " + username);
        }

        return org.springframework.security.core.userdetails.User.withUserDetails(user.get()).build();
    }

}
