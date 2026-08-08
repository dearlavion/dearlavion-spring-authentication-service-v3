package com.dearlavion.authservice.auth;

public record ResetPasswordRequest(String token, String newPassword) {
}
