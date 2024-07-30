package com.devwiki.leafy.dto.userPlant;

import com.devwiki.leafy.dto.plant.PlantSimpleDto;
import com.devwiki.leafy.dto.user.UserResponseDto;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserPlantSimpleDto {
    private Long userPlantId;
    private UserResponseDto user;
    private PlantSimpleDto plant;
    private String plantNickname;
    private Boolean waterRequired;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
