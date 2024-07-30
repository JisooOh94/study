package com.devwiki.leafy.dto.plant;

import com.devwiki.leafy.model.plant.Plant;
import org.springframework.stereotype.Component;

@Component
public class PlantMapper {

    public static PlantDetailDto toDetailDto(Plant plant) {
        PlantDetailDto plantDetailDto = new PlantDetailDto();
        plantDetailDto.setPlantId(plant.getPlantId());
        plantDetailDto.setPlantName(plant.getPlantName());
        plantDetailDto.setPlantType(plant.getPlantType());
        plantDetailDto.setPlantDesc(plant.getPlantDesc());
        plantDetailDto.setImageUrl(plant.getImageUrl());
        plantDetailDto.setTemperatureLow(plant.getTemperatureLow());
        plantDetailDto.setTemperatureHigh(plant.getTemperatureHigh());
        plantDetailDto.setHumidityLow(plant.getHumidityLow());
        plantDetailDto.setHumidityHigh(plant.getHumidityHigh());
        plantDetailDto.setWateringInterval(plant.getWateringInterval());
        plantDetailDto.setCreatedAt(plant.getCreatedAt());
        plantDetailDto.setUpdatedAt(plant.getUpdatedAt());
        return plantDetailDto;
    }


    public static PlantSimpleDto toSimpleDto(Plant plant) {
        PlantSimpleDto plantSimpleDto = new PlantSimpleDto();
        plantSimpleDto.setPlantId(plant.getPlantId());
        plantSimpleDto.setPlantName(plant.getPlantName());
        plantSimpleDto.setPlantType(plant.getPlantType());
        return plantSimpleDto;
    }
}
