package com.dearlavion.authservice.auth.response;

import com.dearlavion.authservice.user.model.AuthType;
import com.dearlavion.authservice.user.model.Role;

/** Login response user object — excludes the password hash. */
public record LoginUserView(
        String id, String username, String email, String firstname, String lastname,
        String phone, String image, Role activeProfile, AuthType type, String customer
) {
}
