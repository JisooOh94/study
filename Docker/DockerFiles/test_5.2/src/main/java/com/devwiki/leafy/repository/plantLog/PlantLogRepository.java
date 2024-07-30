package com.devwiki.leafy.repository.plantLog;

import com.devwiki.leafy.model.plantLog.PlantLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlantLogRepository extends JpaRepository<PlantLog, Long> {
    List<PlantLog> findAllByUserPlant_UserPlantId_OrderByLogDateDesc(Long userPlantId);

    List<PlantLog> findAllByUserPlant_User_UserId_OrderByLogDateDesc(Long userId);
}