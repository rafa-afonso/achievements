package com.rafa.achievements.achievement_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import com.rafa.achievements.achievement_service.controller.request.AchievementCreatedRequest;
import com.rafa.achievements.achievement_service.entity.Achievement;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AchievementMapper {
    Achievement toEntity(AchievementCreatedRequest request);
}
