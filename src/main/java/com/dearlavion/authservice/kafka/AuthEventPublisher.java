package com.dearlavion.authservice.kafka;

public interface AuthEventPublisher {
    void publish(AuthEventType type, Object payload);
}
