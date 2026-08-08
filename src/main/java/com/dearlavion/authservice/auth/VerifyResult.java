package com.dearlavion.authservice.auth;

import com.dearlavion.authservice.user.Role;

/** The shape core/notification/store-engine/booking-engine expect from POST /auth/verify. */
public record VerifyResult(boolean valid, String username, String email, String userId, Role activeProfile, String customer) {
}
