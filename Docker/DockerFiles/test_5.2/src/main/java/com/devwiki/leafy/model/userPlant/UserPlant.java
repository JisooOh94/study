package com.devwiki.leafy.model.userPlant;

import com.devwiki.leafy.dto.userPlant.UserPlantDto;
import com.devwiki.leafy.model.plant.Plant;
import com.devwiki.leafy.model.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_plants")
@Getter
@Builder
public class UserPlant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_plant_id")
    private Long userPlantId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plant_id", nullable = false)
    private Plant plant;

    @Column(name = "plant_nickname")
    private String plantNickname;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public UserPlant(UserPlantDto userPlantDto) {
        this.userPlantId = userPlantDto.getUserPlantId();
        this.user = new User(userPlantDto.getUser());
        this.plant = new Plant(userPlantDto.getPlant());
        this.plantNickname = userPlantDto.getPlantNickname();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void updateEntity(UserPlantDto userPlantDto) {
        this.user = new User(userPlantDto.getUser());
        this.plant = new Plant(userPlantDto.getPlant());
        this.updatedAt = LocalDateTime.now();
    }
}
