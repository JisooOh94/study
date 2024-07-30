package com.devwiki.leafy.dto.plant;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PlantDetailDto {

    private Long plantId;
    private String plantName;
    private String plantType;
    private String plantDesc;
    private String imageUrl;
    private float temperatureLow;
    private float temperatureHigh;
    private float humidityLow;
    private float humidityHigh;
    private int wateringInterval;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
