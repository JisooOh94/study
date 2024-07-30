package com.devwiki.leafy.service.plantLog;

import com.devwiki.leafy.dto.plantLog.PlantLogDto;
import com.devwiki.leafy.dto.plantLog.PlantLogMapper;
import com.devwiki.leafy.dto.plantLog.PlantLogSimpleDto;
import com.devwiki.leafy.dto.userPlant.UserPlantDto;
import com.devwiki.leafy.exception.ResourceNotFoundException;
import com.devwiki.leafy.model.plantLog.PlantLog;
import com.devwiki.leafy.repository.plantLog.PlantLogRepository;
import com.devwiki.leafy.service.userPlant.UserPlantService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlantLogService {

    private final PlantLogRepository plantLogRepository;
    private final UserPlantService userPlantService;

    /**
     * 모든 식물 로그 조회
     *
     * @return 모든 식물 로그 리스트
     */
    public List<PlantLogSimpleDto> getAllPlantLogs() {
        List<PlantLog> plantLogs = plantLogRepository.findAll();
        return plantLogs.stream().map(PlantLogMapper::toSimpleDto).collect(Collectors.toList());
    }

    /**
     * 사용자의 모든 식물 로그 조회
     *
     * @return 모든 식물 로그 리스트
     */
    public List<PlantLogSimpleDto> getAllPlantLogsByUserId(Long userId) {
        List<PlantLog> plantLogs = plantLogRepository.findAllByUserPlant_User_UserId_OrderByLogDateDesc(userId);
        return plantLogs.stream().map(PlantLogMapper::toSimpleDto).collect(Collectors.toList());
    }

    /**
     * 식물 로그 ID로 조회
     *
     * @param plantLogId 조회할 식물 로그 ID
     * @return 조회된 식물 로그
     */
    public PlantLogSimpleDto getPlantLogById(Long plantLogId) {
        PlantLog plantLog = findPlantLogById(plantLogId);
        return PlantLogMapper.toSimpleDto(plantLog);
    }

    /**
     * 유저-식물에 대한 모든 로그 조회
     *
     * @param userPlantId 유저-식물 ID
     * @return 유저-식물에 대한 모든 로그 리스트
     */
    public List<PlantLogSimpleDto> getAllPlantLogsByUserPlantId(Long userPlantId) {
        List<PlantLog> plantLogs = plantLogRepository.findAllByUserPlant_UserPlantId_OrderByLogDateDesc(userPlantId);
        return plantLogs.stream().map(PlantLogMapper::toSimpleDto).collect(Collectors.toList());
    }

    /**
     * 식물 로그 추가
     *
     * @param plantLogDto 추가할 식물 로그
     * @return 추가된 식물 로그
     */
    @Transactional
    public PlantLogSimpleDto addPlantLog(PlantLogDto plantLogDto) {
        plantLogDto.setCreatedAt(LocalDateTime.now());
        plantLogDto.setUpdatedAt(LocalDateTime.now());

        //기존 UserPlant 정보가 조회하는 지 확인
        UserPlantDto userPlantDto = userPlantService.getUserPlantById(plantLogDto.getUserPlant().getUserPlantId());
        plantLogDto.setUserPlant(userPlantDto);

        //Entity 변환 및 저장
        PlantLog plantLog = PlantLogMapper.toEntity(plantLogDto);
        plantLogRepository.save(plantLog);
        return PlantLogMapper.toSimpleDto(plantLog);
    }

    /**
     * 식물 로그 수정
     *
     * @param plantLogDto 수정할 식물 로그 정보
     * @return 수정된 식물 로그 정보
     * @throws ResourceNotFoundException 해당 식물 로그 정보가 존재하지 않을 경우 예외 발생
     */
    @Transactional
    public PlantLogSimpleDto updatePlantLog(Long plnatLogId, PlantLogDto plantLogDto) {
        plantLogDto.setUpdatedAt(LocalDateTime.now());
        PlantLog plantLog = findPlantLogById(plnatLogId);
        plantLog.updateEntity(plantLogDto);
        plantLogRepository.save(plantLog);
        return PlantLogMapper.toSimpleDto(plantLog);
    }


    /**
     * 식물 로그 삭제
     *
     * @param plantLogId 수정할 식물 로그 정보
     * @return 수정된 식물 로그 정보
     * @throws ResourceNotFoundException 해당 식물 로그 정보가 존재하지 않을 경우 예외 발생
     */
    @Transactional
    public void deletePlantLog(Long plantLogId) {
        PlantLog plantLog = findPlantLogById(plantLogId);
        plantLogRepository.delete(plantLog);
    }

    /**
     * 특정 id를 가진 식물 로그 조회
     */
    private PlantLog findPlantLogById(Long plantLogId) {
        return plantLogRepository.findById(plantLogId)
                .orElseThrow(() -> new ResourceNotFoundException("PlantLog", "id", plantLogId));
    }
}