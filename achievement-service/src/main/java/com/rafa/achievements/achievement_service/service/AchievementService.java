package com.rafa.achievements.achievement_service.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rafa.achievements.achievement_service.config.KafkaConfigProps;
import com.rafa.achievements.achievement_service.entity.Achievement;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class AchievementService {

    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final KafkaConfigProps kafkaConfigProps;

    public void sendAchievementData(Achievement achievement) {
        log.info("Sending achievement to kafka: {}", achievement.toString());
        try {
            final String payload = objectMapper.writeValueAsString(achievement);
            kafkaTemplate.send(kafkaConfigProps.getTopic(), payload);
        } catch (final JsonProcessingException ex) {
            throw new RuntimeException("An error has occurred: ", ex);
        }
    }
}
