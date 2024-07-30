package com.devwiki.leafy.dto.plantLog;

import com.devwiki.leafy.dto.userPlant.UserPlantSimpleDto;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class PlantLogSimpleDto {
    private Long plantLogId;
    private UserPlantSimpleDto userPlant;
    private LocalDate logDate;
    private String note;
    private boolean watered;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
