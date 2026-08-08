package com.dearlavion.authservice.auth;

import com.dearlavion.authservice.auth.response.LoginUserView;
import com.dearlavion.authservice.user.model.User;
import org.springframework.stereotype.Component;

@Component
public class AuthMapper {

    /** Login response user object — excludes the password hash. `customer` comes from the
     * request's own X-Customer header, not the User document. */
    public LoginUserView toLoginUserView(User user, String customer) {
        return new LoginUserView(
                user.getId(), user.getUsername(), user.getEmail(), user.getFirstname(), user.getLastname(),
                user.getPhone(), user.getImage(), user.getActiveProfile(), user.getType(), customer
        );
    }
}
