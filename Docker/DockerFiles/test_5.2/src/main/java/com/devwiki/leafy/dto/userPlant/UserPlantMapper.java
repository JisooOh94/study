package com.devwiki.leafy.dto.userPlant;

import com.devwiki.leafy.dto.plant.PlantMapper;
import com.devwiki.leafy.dto.user.UserMapper;
import com.devwiki.leafy.model.userPlant.UserPlant;
import org.springframework.stereotype.Component;

@Component
public class UserPlantMapper {

    public static UserPlantDto toDto(UserPlant userPlant) {
        UserPlantDto userPlantDto = new UserPlantDto();

        userPlantDto.setUserPlantId(userPlant.getUserPlantId());
        userPlantDto.setUser(UserMapper.toDto(userPlant.getUser()));
        userPlantDto.setPlant(PlantMapper.toDetailDto(userPlant.getPlant()));
        userPlantDto.setPlantNickname(userPlant.getPlantNickname());
        userPlantDto.setCreatedAt(userPlant.getCreatedAt());
        userPlantDto.setUpdatedAt(userPlant.getUpdatedAt());
        return userPlantDto;
    }

    public static UserPlantSimpleDto toSimpleDto(UserPlant userPlant) {
        UserPlantSimpleDto userPlantSimpleDto = new UserPlantSimpleDto();

        userPlantSimpleDto.setUserPlantId(userPlant.getUserPlantId());
        userPlantSimpleDto.setUser(UserMapper.toResponseDto(userPlant.getUser()));
        if (userPlant.getPlant() != null) {
            userPlantSimpleDto.setPlant(PlantMapper.toSimpleDto(userPlant.getPlant()));
        }
        userPlantSimpleDto.setPlantNickname(userPlant.getPlantNickname());
        userPlantSimpleDto.setCreatedAt(userPlant.getCreatedAt());
        userPlantSimpleDto.setUpdatedAt(userPlant.getUpdatedAt());
        return userPlantSimpleDto;
    }

    public static UserPlant toEntity(UserPlantDto userPlantDto) {
        return new UserPlant(userPlantDto);
    }

}
