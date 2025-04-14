package com.rafa.achievements.achievement_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.rafa.achievements.achievement_service"})
public class AchievementServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AchievementServiceApplication.class, args);
    }

}
