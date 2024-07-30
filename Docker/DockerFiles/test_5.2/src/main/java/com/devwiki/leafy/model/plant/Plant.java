package com.devwiki.leafy.model.plant;

import com.devwiki.leafy.dto.plant.PlantDetailDto;
import com.devwiki.leafy.dto.plant.PlantSimpleDto;
import lombok.*;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "plants")
@Getter
public class Plant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "plant_id")
    private Long plantId;

    @Column(name = "plant_name", nullable = false)
    private String plantName;

    @Column(name = "plant_type", nullable = false)
    private String plantType;

    @Column(name = "plant_desc", nullable = false)
    private String plantDesc;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "temperature_low", nullable = false)
    private float temperatureLow;

    @Column(name = "temperature_high", nullable = false)
    private float temperatureHigh;

    @Column(name = "humidity_low", nullable = false)
    private float humidityLow;

    @Column(name = "humidity_high", nullable = false)
    private float humidityHigh;

    @Column(name = "watering_interval", nullable = false)
    private int wateringInterval;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public void updateEntity(PlantDetailDto plantDetailDto){
        this.plantName = plantDetailDto.getPlantName();
        this.plantType = plantDetailDto.getPlantType();
        this.plantDesc = plantDetailDto.getPlantDesc();
        this.imageUrl = plantDetailDto.getImageUrl();
        this.temperatureLow = plantDetailDto.getTemperatureLow();
        this.temperatureHigh = plantDetailDto.getTemperatureHigh();
        this.humidityLow = plantDetailDto.getHumidityLow();
        this.humidityHigh = plantDetailDto.getHumidityHigh();
        this.wateringInterval = plantDetailDto.getWateringInterval();
        this.updatedAt = LocalDateTime.now();
    }

    public Plant(PlantDetailDto plantDetailDto) {
        this.plantId = plantDetailDto.getPlantId();
        this.plantName = plantDetailDto.getPlantName();
        this.plantType = plantDetailDto.getPlantType();
        this.plantDesc = plantDetailDto.getPlantDesc();
        this.imageUrl = plantDetailDto.getImageUrl();
        this.temperatureLow = plantDetailDto.getTemperatureLow();
        this.temperatureHigh = plantDetailDto.getTemperatureHigh();
        this.humidityLow = plantDetailDto.getHumidityLow();
        this.humidityHigh = plantDetailDto.getHumidityHigh();
        this.wateringInterval = plantDetailDto.getWateringInterval();
        this.createdAt = plantDetailDto.getCreatedAt();
        this.updatedAt = plantDetailDto.getUpdatedAt();
    }


    public Plant(PlantSimpleDto plantSimpleDto) {
        this.plantId = plantSimpleDto.getPlantId();
        this.plantName = plantSimpleDto.getPlantName();
        this.plantType = plantSimpleDto.getPlantType();
        this.plantDesc = plantSimpleDto.getPlantDesc();
        this.imageUrl = plantSimpleDto.getImageUrl();
        this.temperatureLow = plantSimpleDto.getTemperatureLow();
        this.temperatureHigh = plantSimpleDto.getTemperatureHigh();
        this.humidityLow = plantSimpleDto.getHumidityLow();
        this.humidityHigh = plantSimpleDto.getHumidityHigh();
        this.wateringInterval = plantSimpleDto.getWateringInterval();
        this.createdAt = plantSimpleDto.getCreatedAt();
        this.updatedAt = plantSimpleDto.getUpdatedAt();
    }
}
