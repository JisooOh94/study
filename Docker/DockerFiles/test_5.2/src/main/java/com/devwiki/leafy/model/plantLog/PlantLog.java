package com.devwiki.leafy.model.plantLog;

import com.devwiki.leafy.dto.plantLog.PlantLogDto;
import com.devwiki.leafy.model.userPlant.UserPlant;
import lombok.*;
import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "plant_logs")
@Getter
public class PlantLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "plant_log_id")
    private Long plantLogId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_plant_id")
    private UserPlant userPlant;

    @Column(name = "log_date")
    private LocalDate logDate;

    @Column(name = "note")
    private String note;

    @Column(name = "watered")
    private boolean watered;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public void updateEntity(PlantLogDto plantLogDto){
        this.logDate = plantLogDto.getLogDate();
        this.note = plantLogDto.getNote();
        this.watered = plantLogDto.isWatered();
        this.updatedAt = LocalDateTime.now();
    }
}

