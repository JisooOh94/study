package com.devwiki.leafy.repository.plant;

import com.devwiki.leafy.model.plant.Plant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlantRepository extends JpaRepository<Plant, Long> {
}
