package com.dearlavion.authservice.health;

import com.mongodb.client.MongoClient;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/** Kept at /actuator/health to match the original Java v1 service's path (the NestJS v2 port
 * preserved this too, for the same reason). */
@RestController
@RequiredArgsConstructor
public class HealthController {

    private final MongoClient mongoClient;

    @GetMapping("/actuator/health")
    public Map<String, String> health() {
        try {
            mongoClient.listDatabaseNames().first();
            return Map.of("status", "UP");
        } catch (Exception e) {
            return Map.of("status", "DOWN");
        }
    }
}
