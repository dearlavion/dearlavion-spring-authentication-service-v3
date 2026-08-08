package com.dearlavion.authservice.auth;

public record LoginResponse(String token, LoginUserView user) {
}
