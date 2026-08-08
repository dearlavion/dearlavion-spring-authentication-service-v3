package com.dearlavion.authservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/** BCrypt at the default strength (10) — interoperable with the original Java v1 service's own
 * BCryptPasswordEncoder and the NestJS v2 service's bcryptjs hashes (all produce/verify $2a/$2b). */
@Configuration
public class PasswordEncoderConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
