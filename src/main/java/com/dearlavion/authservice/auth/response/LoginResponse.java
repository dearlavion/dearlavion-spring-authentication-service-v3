package com.dearlavion.authservice.auth.response;

public record LoginResponse(String token, LoginUserView user) {
}
