package com.dearlavion.authservice.auth;

import com.dearlavion.authservice.user.model.AuthType;
import com.dearlavion.authservice.user.model.User;
import com.dearlavion.authservice.user.request.UserVoRequest;

public interface AuthStrategy {
    AuthType type();

    User authenticate(UserVoRequest vo, String customer);

    User register(UserVoRequest vo, String customer);
}
