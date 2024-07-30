package com.devwiki.leafy.dto.plantLog;

import com.devwiki.leafy.dto.userPlant.UserPlantMapper;
import com.devwiki.leafy.model.plantLog.PlantLog;
import org.springframework.stereotype.Component;

@Component
public class PlantLogMapper {

    public static PlantLogDto toDto(PlantLog plantLog) {
        PlantLogDto plantLogDto = new PlantLogDto();
        plantLogDto.setPlantLogId(plantLog.getPlantLogId());
        plantLogDto.setUserPlant(UserPlantMapper.toDto(plantLog.getUserPlant()));
        plantLogDto.setLogDate(plantLog.getLogDate());
        plantLogDto.setNote(plantLog.getNote());
        plantLogDto.setWatered(plantLog.isWatered());
        plantLogDto.setCreatedAt(plantLog.getCreatedAt());
        plantLogDto.setUpdatedAt(plantLog.getUpdatedAt());
        return plantLogDto;
    }


    public static PlantLogSimpleDto toSimpleDto(PlantLog plantLog) {
        PlantLogSimpleDto plantLogSimpleDto = new PlantLogSimpleDto();
        plantLogSimpleDto.setPlantLogId(plantLog.getPlantLogId());
        plantLogSimpleDto.setUserPlant(UserPlantMapper.toSimpleDto(plantLog.getUserPlant()));
        plantLogSimpleDto.setLogDate(plantLog.getLogDate());
        plantLogSimpleDto.setNote(plantLog.getNote());
        plantLogSimpleDto.setWatered(plantLog.isWatered());
        plantLogSimpleDto.setCreatedAt(plantLog.getCreatedAt());
        plantLogSimpleDto.setUpdatedAt(plantLog.getUpdatedAt());
        return plantLogSimpleDto;
    }

    public static PlantLog toEntity(PlantLogDto plantLogDto) {
        return new PlantLog(
                plantLogDto.getPlantLogId(),
                UserPlantMapper.toEntity(plantLogDto.getUserPlant()),
                plantLogDto.getLogDate(),
                plantLogDto.getNote(),
                plantLogDto.isWatered(),
                plantLogDto.getCreatedAt(),
                plantLogDto.getUpdatedAt()
        );
    }

}
