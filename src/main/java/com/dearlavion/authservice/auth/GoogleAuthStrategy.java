package com.dearlavion.authservice.auth;

import com.dearlavion.authservice.common.exception.ConflictException;
import com.dearlavion.authservice.common.exception.UnauthorizedException;
import com.dearlavion.authservice.user.AuthType;
import com.dearlavion.authservice.user.User;
import com.dearlavion.authservice.user.UserService;
import com.dearlavion.authservice.user.UserVoRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/** Google ID-token authentication (matches the original Java GoogleAuthenticationStrategy and the
 * NestJS GoogleAuthStrategy). */
@Component
@RequiredArgsConstructor
public class GoogleAuthStrategy implements AuthStrategy {

    private final UserService userService;
    private final GoogleVerifierService googleVerifier;

    @Override
    public AuthType type() {
        return AuthType.GOOGLE;
    }

    @Override
    public User authenticate(UserVoRequest vo, String customer) {
        GoogleIdToken.Payload payload = googleVerifier.verify(vo.googleToken() != null ? vo.googleToken() : "");
        User user = payload.getEmail() != null ? userService.findByEmail(customer, payload.getEmail()) : null;
        if (user == null) {
            throw new UnauthorizedException("User not registered. Please sign up first.");
        }
        return user;
    }

    @Override
    public User register(UserVoRequest vo, String customer) {
        GoogleIdToken.Payload payload = googleVerifier.verify(vo.googleToken() != null ? vo.googleToken() : "");
        Boolean emailVerified = payload.getEmailVerified();
        if (emailVerified == null || !emailVerified) {
            throw new UnauthorizedException("Google authentication failed");
        }
        // Security check: the Google account email must match the submitted email.
        if (!payload.getEmail().equals(vo.email())) {
            throw new UnauthorizedException("Email does not match Google account");
        }
        if (vo.email() != null && userService.findByEmail(customer, vo.email()) != null) {
            throw new ConflictException("User already exists");
        }
        return userService.registerUser(customer, vo, AuthType.GOOGLE);
    }
}
