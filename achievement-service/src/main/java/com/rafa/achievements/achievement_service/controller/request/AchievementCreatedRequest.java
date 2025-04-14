package com.rafa.achievements.achievement_service.controller.request;

import jakarta.validation.constraints.NotBlank;

public record AchievementCreatedRequest(
    @NotBlank String name,
    String description
) {
}
