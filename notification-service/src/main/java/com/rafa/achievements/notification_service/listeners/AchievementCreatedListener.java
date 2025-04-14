package com.rafa.achievements.notification_service.listeners;

import com.rafa.achievements.notification_service.config.KafkaConfigProps;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class AchievementCreatedListener {

    private final KafkaConfigProps kafkaConfigProps;

    @KafkaListener(topics = "${achievements.kafka.listener.topic}")
    public void listens(final String in) {
        log.info("Listening to topic: {}", kafkaConfigProps.listener()
            .topic());
        log.info("Received message: {}", in);
    }

}
