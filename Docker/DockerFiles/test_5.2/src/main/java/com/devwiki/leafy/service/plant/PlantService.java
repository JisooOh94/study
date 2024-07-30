package com.devwiki.leafy.service.plant;

import com.devwiki.leafy.dto.plant.PlantDetailDto;
import com.devwiki.leafy.dto.plant.PlantMapper;
import com.devwiki.leafy.exception.ResourceNotFoundException;
import com.devwiki.leafy.model.plant.Plant;
import com.devwiki.leafy.repository.plant.PlantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlantService {

    private final PlantRepository plantRepository;

    /**
     * 모든 식물 정보 조회
     *
     * @return 모든 식물 정보
     */
    public List<PlantDetailDto> getAllPlants() {
        return plantRepository.findAll()
                .stream()
                .map(PlantMapper::toDetailDto)
                .sorted(Comparator.comparing(PlantDetailDto::getCreatedAt).reversed())
                .collect(Collectors.toList());
    }

    /**
     * 특정 식물 정보 조회
     *
     * @param plantId 조회할 식물 id
     * @return 특정 식물 정보
     */
    public PlantDetailDto getPlantById(Long plantId) {
        Plant plant = findPlantById(plantId);
        return PlantMapper.toDetailDto(plant);
    }

    /**
     * 식물 정보 추가
     *
     * @param plantDetailDto 추가할 식물 정보
     * @return 추가된 식물 정보
     */
    @Transactional
    public PlantDetailDto addPlant(PlantDetailDto plantDetailDto) {
        plantDetailDto.setCreatedAt(LocalDateTime.now());
        plantDetailDto.setUpdatedAt(LocalDateTime.now());
        Plant plant = new Plant(plantDetailDto);
        plantRepository.save(plant);
        return PlantMapper.toDetailDto(plant);
    }

    /**
     * 식물 정보 수정
     *
     * @param plantDetailDto 수정할 식물 정보
     * @return 수정된 식물 정보
     * @throws ResourceNotFoundException 해당 식물 정보가 존재하지 않을 경우 예외 발생
     */
    @Transactional
    public PlantDetailDto updatePlant(Long plantId, PlantDetailDto plantDetailDto) {
        Plant plant = findPlantById(plantId);
        plant.updateEntity(plantDetailDto);
        plantRepository.save(plant);
        return PlantMapper.toDetailDto(plant);
    }

    /**
     * 식물 정보 삭제
     *
     * @param plantId 삭제할 식물 id
     * @throws ResourceNotFoundException 해당 식물 정보가 존재하지 않을 경우 예외 발생
     */
    @Transactional
    public void deletePlant(Long plantId) {
        Plant plant = findPlantById(plantId);
        plantRepository.delete(plant);
    }

    /**
     * 특정 id를 가진 식물 정보 조회
     *
     * @param plantId 조회할 식물 id
     * @return 조회된 식물 정보
     * @throws ResourceNotFoundException 해당 식물 정보가 존재하지 않을 경우 예외 발생
     */
    private Plant findPlantById(Long plantId) {
        return plantRepository.findById(plantId)
                .orElseThrow(() -> new ResourceNotFoundException("Plant", "id", plantId));
    }

}
