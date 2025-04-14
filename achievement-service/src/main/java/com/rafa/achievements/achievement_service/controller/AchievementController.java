package com.rafa.achievements.achievement_service.controller;

import jakarta.validation.Valid;

import com.rafa.achievements.achievement_service.controller.request.AchievementCreatedRequest;
import com.rafa.achievements.achievement_service.entity.Achievement;
import com.rafa.achievements.achievement_service.mapper.AchievementMapper;
import com.rafa.achievements.achievement_service.service.AchievementService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/achievement")
public class AchievementController {

    private final AchievementMapper mapper;
    private final AchievementService service;

    @PostMapping("/create")
    public ResponseEntity<String> event(@Valid @RequestBody AchievementCreatedRequest request) {
        log.info("New achievement received: {}", request);

        Achievement achievement = mapper.toEntity(request);
        service.sendAchievementData(achievement);

        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

}
