package com.modsen.auth.service.impl;

import com.modsen.auth.dto.request.RegistrationFields;
import com.modsen.auth.dto.request.RoleRequest;
import com.modsen.auth.dto.response.UserResponse;
import com.modsen.auth.exception.InvalidRoleException;
import com.modsen.auth.exception.UserAlreadyExistsException;
import com.modsen.auth.exception.UserNotFoundException;
import com.modsen.auth.mapper.UserMapper;
import com.modsen.auth.security.user.Role;
import com.modsen.auth.model.User;
import com.modsen.auth.repository.UserRepository;
import com.modsen.auth.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.StreamSupport;

/**
 * @author Alexander Dudkin
 */
@Service
public class UserServiceImpl implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserMapper userMapper, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void createUser(RegistrationFields request) {
        String username = request.username();
        log.debug("createUser: Attempting to create user with username: {}", username);
        if (userRepository.existsByUsername(username)) {
            log.warn("createUser: User already exists with username: {}", username);
            throw new UserAlreadyExistsException("User already exists with username: " + username);
        }

        User user = userMapper.toUser(request);
        log.debug("createUser: Mapped registration fields to User: {}", user);
        userRepository.save(user.withEncryptedPassword(passwordEncoder.encode(request.password())));
        log.info("createUser: Successfully created user with username: {}", username);
    }

    @Override
    @Transactional
    public void updateUser(UUID id, RegistrationFields request) {
        log.debug("updateUser: Attempting update for user id: {}", id);
        User user = getOrThrow(id);
        String username = request.username();

        if (!user.getUsername().equals(username)) {
            if (userRepository.existsByUsername(username)) {
                log.warn("updateUser: Duplicate username detected: {}", username);
                throw new UserAlreadyExistsException("User already exists with username: " + username);
            }
        }

        user = userMapper.toUser(request);
        log.debug("updateUser: Mapped registration fields to User: {}", user);
        userRepository.update(id, user.withEncryptedPassword(passwordEncoder.encode(request.password())));
        log.info("updateUser: Successfully updated user with ID: {}", id);
    }

    @Override
    @Transactional
    public void assignRole(UUID id, RoleRequest request) {
        log.debug("assignRole: Attempting to assign role {} to user id: {}", request.role(), id);
        User user = getOrThrow(id);
        String roleName = request.role();
        try {
            Role role = Role.valueOf(roleName);
            userRepository.assignRole(user, role);
            log.info("assignRole: Successfully assigned role {} to user {}", role, user.getUsername());
        } catch (Exception e) {
            log.error("assignRole: Unknown role '{}' for user id: {}", roleName, id, e);
            throw new InvalidRoleException("Can't assign role. Unknown role: " + roleName);
        }
    }

    @Override
    @Transactional
    public void deleteUser(UUID id) {
        log.debug("deleteUser: Attempting to delete user id: {}", id);
        User user = getOrThrow(id);
        userRepository.delete(user);
        log.info("deleteUser: Successfully deleted user with id: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserResponse> getUserById(UUID userId) {
        log.debug("getUserById: Fetching user by id: {}", userId);
        if (!userRepository.existsById(userId)) {
            log.warn("getUserById: No user found with id: {}", userId);
            throw new UserNotFoundException("User not found with ID: " + userId);
        }
        Optional<UserResponse> response = userRepository.findById(userId)
                .map(userMapper::toResponse);
        log.info("getUserById: Retrieved user response for id: {}", userId);
        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserResponse> getUserByUsername(String username) {
        log.debug("getUserByUsername: Fetching user by username: {}", username);
        if (!userRepository.existsByUsername(username)) {
            log.warn("getUserByUsername: No user found with username: {}", username);
            throw new UserNotFoundException("User not found with username: " + username);
        }
        Optional<UserResponse> response = userRepository.findByUsername(username)
                .map(userMapper::toResponse);
        log.info("getUserByUsername: Retrieved user response for username: {}", username);
        return response;
    }

    @Override
    public Collection<UserResponse> getAllUsers(int page, int size) {
        log.debug("getAllUsers: Fetching all users on page={} with size={}", page, size);
        List<UserResponse> response = StreamSupport.stream(userRepository.findAll(page, size).spliterator(), false)
                .map(userMapper::toResponse)
                .toList();
        log.info("getAllUsers: Retrieved {} users", response.size());
        return response;
    }

    private User getOrThrow(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("getOrThrow: User not found with id: {}", id);
                    return new UserNotFoundException("User not found with ID: " + id);
                });
    }

}
