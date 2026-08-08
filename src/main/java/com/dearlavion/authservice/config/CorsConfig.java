package com.dearlavion.authservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/** Matches the NestJS v2 bootstrap's enableCors() origin list, plus any extra origins from
 * FRONTEND_ORIGINS (comma-separated) — e.g. a deployed customer UI's public origin, so a new
 * tenant UI doesn't need a code change. Implemented as a plain CorsFilter bean since this service
 * doesn't otherwise use Spring Security (see AdminAuthFilter's own comment on that choice). */
@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter(@Value("${app.frontend-origins}") String frontendOriginsCsv) {
        List<String> origins = new ArrayList<>(List.of(
                "http://dearlavion.site",
                "https://dearlavion.site",
                "https://www.dearlavion.site",
                "https://*.ngrok.pizza",
                "http://localhost:4200"
        ));
        if (frontendOriginsCsv != null && !frontendOriginsCsv.isBlank()) {
            origins.addAll(Arrays.stream(frontendOriginsCsv.split(",")).map(String::trim).filter(s -> !s.isEmpty()).toList());
        }

        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(origins);
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return new CorsFilter(source);
    }
}
