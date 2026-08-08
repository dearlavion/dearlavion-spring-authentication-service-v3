package com.dearlavion.authservice.auth.request;

public record ResetPasswordRequest(String token, String newPassword) {
}
