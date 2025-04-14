package com.rafa.achievements.notification_service.config;

import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = "achievements.kafka")
public record KafkaConfigProps(
    ListenerProps listener
) {
}
