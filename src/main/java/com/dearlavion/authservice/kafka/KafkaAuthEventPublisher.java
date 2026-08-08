package com.dearlavion.authservice.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.LinkedHashMap;
import java.util.Map;

/** Sends the {type, payload} envelope to authentication-service-event — same shape the original
 * Java v1 and NestJS v2 services publish, so notification-service's consumer needs no changes. */
@Slf4j
@RequiredArgsConstructor
public class KafkaAuthEventPublisher implements AuthEventPublisher {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public void publish(AuthEventType type, Object payload) {
        try {
            Map<String, Object> envelope = new LinkedHashMap<>();
            envelope.put("type", type.name());
            envelope.put("payload", payload);
            kafkaTemplate.send(KafkaTopicConfig.AUTH_TOPIC, objectMapper.writeValueAsString(envelope));
        } catch (Exception e) {
            log.error("Failed to publish {}: {}", type, e.getMessage());
        }
    }
}
