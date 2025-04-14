package com.rafa.achievements.achievement_service.config;

import org.apache.kafka.clients.admin.NewTopic;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    @Bean
    public NewTopic booksPublishedTopic(final KafkaConfigProps kafkaConfigProps) {
        return TopicBuilder.name(kafkaConfigProps.getTopic())
            .partitions(2)
            .replicas(2)
            .build();
    }
}
