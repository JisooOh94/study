package com.devwiki.leafy.controller.plantLog;

import com.devwiki.leafy.dto.plantLog.PlantLogDto;
import com.devwiki.leafy.dto.plantLog.PlantLogSimpleDto;
import com.devwiki.leafy.service.plantLog.PlantLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/plant-logs")
@RequiredArgsConstructor
public class PlantLogController {

    private final PlantLogService plantLogService;

    /**
     * 모든 식물 로그 조회
     *
     * @return 모든 식물 로그 리스트
     */
    @GetMapping("")
    public ResponseEntity<List<PlantLogSimpleDto>> getAllPlantLogs() {
        List<PlantLogSimpleDto> plantLogDtoList = plantLogService.getAllPlantLogs();
        return new ResponseEntity<>(plantLogDtoList, HttpStatus.OK);
    }

    /**
     * 특정 사용자의 모든 식물 로그 조회
     *
     * @return 모든 식물 로그 리스트
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PlantLogSimpleDto>> getAllPlantLogsByUserId(@PathVariable Long userId) {
        List<PlantLogSimpleDto> plantLogDtoList = plantLogService.getAllPlantLogsByUserId(userId);
        return new ResponseEntity<>(plantLogDtoList, HttpStatus.OK);
    }


    /**
     * 특정 사용자의 모든 식물 로그 조회(최근 5건)
     *
     * @return 모든 식물 로그 리스트
     */
    @GetMapping("/recent/user/{userId}")
    public ResponseEntity<List<PlantLogSimpleDto>> getAllPlantRecentLogsByUserId(@PathVariable Long userId) {
        List<PlantLogSimpleDto> plantLogDtoList = plantLogService.getAllPlantLogsByUserId(userId)
                .stream().limit(4)
                .collect(Collectors.toList());
        return new ResponseEntity<>(plantLogDtoList, HttpStatus.OK);
    }

    /**
     * 식물 로그 ID로 조회
     *
     * @param plantLogId 조회할 식물 로그 ID
     * @return 조회된 식물 로그
     */
    @GetMapping("/{plantLogId}")
    public ResponseEntity<PlantLogSimpleDto> getPlantLogById(@PathVariable Long plantLogId) {
        PlantLogSimpleDto plantLogDto = plantLogService.getPlantLogById(plantLogId);
        return new ResponseEntity<>(plantLogDto, HttpStatus.OK);
    }

    /**
     * 유저-식물에 대한 모든 로그 조회
     *
     * @param userPlantId 유저-식물 ID
     * @return 유저-식물에 대한 모든 로그 리스트
     */
    @GetMapping("/user-plants/{userPlantId}")
    public ResponseEntity<List<PlantLogSimpleDto>> getAllPlantLogsByUserPlantId(@PathVariable Long userPlantId) {
        List<PlantLogSimpleDto> plantLogDtoList = plantLogService.getAllPlantLogsByUserPlantId(userPlantId);
        return new ResponseEntity<>(plantLogDtoList, HttpStatus.OK);
    }

    /**
     * 식물 로그 추가
     *
     * @param plantLogDto 추가할 식물 로그
     * @return 추가된 식물 로그
     */
    @PostMapping("")
    public ResponseEntity<PlantLogSimpleDto> addPlantLog(@RequestBody @Valid PlantLogDto plantLogDto) {
        PlantLogSimpleDto addedPlantLogDto = plantLogService.addPlantLog(plantLogDto);
        return new ResponseEntity<>(addedPlantLogDto, HttpStatus.CREATED);
    }

    /**
     * 식물 로그 수정
     *
     * @param plantLogId  수정할 식물 로그 Id
     * @param plantLogDto 수정할 식물 로그 정보
     * @return 수정된 식물 로그 정보
     */
    @PutMapping("/{plantLogId}")
    public ResponseEntity<PlantLogSimpleDto> updatePlantLog(@PathVariable Long plantLogId, @RequestBody @Valid PlantLogDto plantLogDto) {
        PlantLogSimpleDto updatedPlantLogDto = plantLogService.updatePlantLog(plantLogId, plantLogDto);
        return new ResponseEntity<>(updatedPlantLogDto, HttpStatus.OK);
    }

    /**
     * 식물 로그 삭제
     *
     * @param plantLogId 삭제할 식물 로그 ID
     */
    @DeleteMapping("/{plantLogId}")
    public ResponseEntity<Void> deletePlantLog(@PathVariable Long plantLogId) {
        plantLogService.deletePlantLog(plantLogId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
