package com.dearlavion.authservice.auth;

import com.dearlavion.authservice.common.exception.BadRequestException;
import com.dearlavion.authservice.kafka.AuthEventPublisher;
import com.dearlavion.authservice.kafka.AuthEventType;
import com.dearlavion.authservice.user.AuthType;
import com.dearlavion.authservice.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final SimpleAuthStrategy simpleAuthStrategy;
    private final GoogleAuthStrategy googleAuthStrategy;
    private final AuthEventPublisher events;

    private List<AuthStrategy> strategies() {
        return List.of(simpleAuthStrategy, googleAuthStrategy);
    }

    public AuthStrategy resolve(AuthType type) {
        return strategies().stream()
                .filter(s -> s.type() == type)
                .findFirst()
                .orElseThrow(() -> new BadRequestException("Unsupported login type"));
    }

    /** Publishes the new-user welcome event (consumed by notification-service). */
    public void sendNewUserWelcomeEmail(User user) {
        events.publish(AuthEventType.NEW_USER, Map.of("username", user.getUsername()));
    }

    /** Publishes the password-reset event carrying the reset token + recipient email. */
    public void sendResetPasswordEvent(User user, String token) {
        events.publish(AuthEventType.RESET_PASSWORD, Map.of(
                "username", user.getUsername(),
                "email", user.getEmail() != null ? user.getEmail() : "",
                "token", token
        ));
    }
}
