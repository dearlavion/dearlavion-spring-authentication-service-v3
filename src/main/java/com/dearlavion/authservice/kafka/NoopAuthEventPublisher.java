package com.dearlavion.authservice.kafka;

public class NoopAuthEventPublisher implements AuthEventPublisher {
    @Override
    public void publish(AuthEventType type, Object payload) {
        // no-op
    }
}
