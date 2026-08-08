package com.dearlavion.authservice.auth;

import com.dearlavion.authservice.user.AuthType;
import com.dearlavion.authservice.user.User;
import com.dearlavion.authservice.user.UserVoRequest;

public interface AuthStrategy {
    AuthType type();

    User authenticate(UserVoRequest vo, String customer);

    User register(UserVoRequest vo, String customer);
}
