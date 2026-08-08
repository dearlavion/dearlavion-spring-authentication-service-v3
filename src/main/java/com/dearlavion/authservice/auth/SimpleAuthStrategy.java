package com.dearlavion.authservice.auth;

import com.dearlavion.authservice.common.exception.ConflictException;
import com.dearlavion.authservice.common.exception.UnauthorizedException;
import com.dearlavion.authservice.user.model.AuthType;
import com.dearlavion.authservice.user.model.User;
import com.dearlavion.authservice.user.UserService;
import com.dearlavion.authservice.user.request.UserVoRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/** Username/email + password authentication (matches the original Java SimpleAuthenticationStrategy
 * and the NestJS SimpleAuthStrategy). */
@Component
@RequiredArgsConstructor
public class SimpleAuthStrategy implements AuthStrategy {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AuthType type() {
        return AuthType.SIMPLE;
    }

    @Override
    public User authenticate(UserVoRequest vo, String customer) {
        User user = vo.username() != null ? userService.findByUsername(customer, vo.username()) : null;
        if (user == null && vo.email() != null) {
            user = userService.findByEmail(customer, vo.email());
        }
        if (user == null || user.getPassword() == null
                || !passwordEncoder.matches(vo.password() != null ? vo.password() : "", user.getPassword())) {
            throw new UnauthorizedException("Invalid credentials");
        }
        return user;
    }

    @Override
    public User register(UserVoRequest vo, String customer) {
        if (vo.email() != null && userService.findByEmail(customer, vo.email()) != null) {
            throw new ConflictException("User already exists");
        }
        return userService.registerUser(customer, vo, AuthType.SIMPLE);
    }
}
