package com.dearlavion.authservice.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaTemplate;

@Slf4j
@Configuration
public class KafkaTopicConfig {

    public static final String AUTH_TOPIC = "authentication-service-event";

    @Bean
    @ConditionalOnProperty(name = "app.kafka-enabled", havingValue = "true")
    public NewTopic authTopic() {
        return TopicBuilder.name(AUTH_TOPIC).partitions(1).replicas(1).build();
    }

    @Bean
    public AuthEventPublisher authEventPublisher(
            @Value("${app.kafka-enabled}") boolean kafkaEnabled,
            KafkaTemplate<String, String> kafkaTemplate,
            ObjectMapper objectMapper
    ) {
        if (!kafkaEnabled) {
            log.info("app.kafka-enabled=false — auth events will not be published");
            return new NoopAuthEventPublisher();
        }
        return new KafkaAuthEventPublisher(kafkaTemplate, objectMapper);
    }
}
