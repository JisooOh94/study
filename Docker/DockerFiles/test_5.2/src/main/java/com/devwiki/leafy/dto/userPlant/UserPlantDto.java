package com.devwiki.leafy.dto.userPlant;

import com.devwiki.leafy.dto.plant.PlantDetailDto;
import com.devwiki.leafy.dto.plant.PlantSimpleDto;
import com.devwiki.leafy.dto.user.UserDto;
import com.devwiki.leafy.dto.user.UserResponseDto;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserPlantDto {

    private Long userPlantId;
    private UserDto user;
    private PlantDetailDto plant;
    private String plantNickname;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
