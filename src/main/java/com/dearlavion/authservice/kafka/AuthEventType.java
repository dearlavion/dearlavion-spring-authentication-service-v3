package com.dearlavion.authservice.kafka;

/** Matches the Java v1 / NestJS v2 EventType so notification-service consumes v3 events unchanged. */
public enum AuthEventType {
    RESET_PASSWORD,
    NEW_USER
}
