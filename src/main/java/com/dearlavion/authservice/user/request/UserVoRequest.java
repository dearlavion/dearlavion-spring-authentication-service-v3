package com.dearlavion.authservice.user.request;

import com.dearlavion.authservice.user.model.Role;

/** Mirrors the NestJS UserVoDto used across register/login/update — every field optional; which
 * ones matter depends on the endpoint/strategy consuming it, exactly like the TS source. */
public record UserVoRequest(
        String username,
        String firstname,
        String lastname,
        String email,
        String phone,
        String password,
        String image,
        Role activeProfile,
        String googleToken
) {
    /** Convenience for endpoints that need to stamp in a googleToken query param without a new
     * record (records are immutable, unlike the TS DTO's mutable class instance). */
    public UserVoRequest withGoogleToken(String token) {
        return new UserVoRequest(username, firstname, lastname, email, phone, password, image, activeProfile, token);
    }

    public UserVoRequest withActiveProfile(Role role) {
        return new UserVoRequest(username, firstname, lastname, email, phone, password, image, role, googleToken);
    }
}
