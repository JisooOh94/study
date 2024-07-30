package com.devwiki.leafy.dto.plant;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PlantSimpleDto {

    private Long plantId;
    private String plantName;
    private String plantType;
    @JsonIgnore
    private String plantDesc;
    @JsonIgnore
    private String imageUrl;
    @JsonIgnore
    private float temperatureLow;
    @JsonIgnore
    private float temperatureHigh;
    @JsonIgnore
    private float humidityLow;
    @JsonIgnore
    private float humidityHigh;
    @JsonIgnore
    private int wateringInterval;
    @JsonIgnore
    private LocalDateTime createdAt;
    @JsonIgnore
    private LocalDateTime updatedAt;
}
