package com.devwiki.leafy.controller.plant;

import com.devwiki.leafy.dto.plant.PlantDetailDto;
import com.devwiki.leafy.service.plant.PlantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/plants")
@RequiredArgsConstructor
public class PlantController {

    private final PlantService plantService;

    /**
     * 모든 식물 조회
     *
     * @return 모든 식물 리스트
     */
    @GetMapping("")
    public ResponseEntity<List<PlantDetailDto>> getAllPlants() {
        List<PlantDetailDto> plantDetailDtoList = plantService.getAllPlants();
        return new ResponseEntity<>(plantDetailDtoList, HttpStatus.OK);
    }

    /**
     * 식물 ID로 조회
     *
     * @param plantId 조회할 식물 ID
     * @return 조회된 식물
     */
    @GetMapping("/{plantId}")
    public ResponseEntity<PlantDetailDto> getPlantById(@PathVariable Long plantId) {
        PlantDetailDto plantDetailDto = plantService.getPlantById(plantId);
        return new ResponseEntity<>(plantDetailDto, HttpStatus.OK);
    }

    /**
     * 식물 추가
     *
     * @param plantDetailDto 추가할 식물
     * @return 추가된 식물
     */
    @PostMapping("")
    public ResponseEntity<PlantDetailDto> addPlant(@Valid @RequestBody PlantDetailDto plantDetailDto) {
        PlantDetailDto addedPlantDetailDto = plantService.addPlant(plantDetailDto);
        return new ResponseEntity<>(addedPlantDetailDto, HttpStatus.CREATED);
    }


    /**
     * 식물 수정
     *
     * @param plantId   수정할 식물 ID
     * @param plantDetailDto  수정할 식물 정보
     * @return 수정된 식물 정보
     */
    @PutMapping("/{plantId}")
    public ResponseEntity<PlantDetailDto> updatePlant(@PathVariable Long plantId, @RequestBody PlantDetailDto plantDetailDto) {
        PlantDetailDto updatedPlantDetailDto = plantService.updatePlant(plantId, plantDetailDto);
        return new ResponseEntity<>(updatedPlantDetailDto, HttpStatus.OK);
    }

    /**
     * 식물 삭제
     *
     * @param plantId 삭제할 식물 ID
     */
    @DeleteMapping("/{plantId}")
    public ResponseEntity<Void> deletePlant(@PathVariable Long plantId) {
        plantService.deletePlant(plantId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
