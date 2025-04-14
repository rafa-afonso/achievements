package com.rafa.achievements.achievement_service.entity;

import java.util.UUID;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import lombok.*;

@Entity
@Getter
@Setter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@ToString
public class Achievement {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false)
    private UUID id;

    @Column(nullable = false)
    @NotBlank
    private String name;

    @Column
    private String description;

}
