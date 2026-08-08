package com.dearlavion.authservice.auth;

import com.dearlavion.authservice.common.exception.UnauthorizedException;
import com.dearlavion.authservice.security.JwtTokenService;
import com.dearlavion.authservice.security.TokenClaims;
import com.dearlavion.authservice.user.model.User;
import com.dearlavion.authservice.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PasswordResetService {

    private final UserService userService;
    private final JwtTokenService jwtTokenService;
    private final AuthService authService;

    /** Starts the reset flow. Silently does nothing if the email is unknown, so the response never
     * reveals whether an account exists. */
    public void initiateReset(String customer, String email) {
        User user = userService.findByEmail(customer, email);
        if (user == null) return;
        String token = jwtTokenService.generatePasswordResetToken(user.getUsername(), customer);
        authService.sendResetPasswordEvent(user, token);
    }

    /** Resets the password given a valid reset token. Throws 401 if the token is invalid/expired.
     * The customer/tenant is taken from the token claim, so no header is needed here. */
    public void resetPassword(String token, String newPassword) {
        TokenClaims claims = jwtTokenService.validatePasswordResetToken(token);
        if (claims.customer() == null) throw new UnauthorizedException("Invalid or expired token");
        userService.updatePassword(claims.customer(), claims.username(), newPassword);
    }
}
