package com.rafa.achievements.notification_service.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(KafkaConfigProps.class)
public class KafkaConfig {
}
