package com.devwiki.leafy.controller.userPlant;

import com.devwiki.leafy.dto.userPlant.UserPlantDto;
import com.devwiki.leafy.dto.userPlant.UserPlantSimpleDto;
import com.devwiki.leafy.service.userPlant.UserPlantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/user-plants")
@RequiredArgsConstructor
public class UserPlantController {

    private final UserPlantService userPlantService;

    /**
     * 사용자가 가지고 있는 모든 식물 정보 조회
     *
     * @param userId 조회할 사용자 id
     * @return 사용자가 가지고 있는 모든 식물 정보
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<UserPlantSimpleDto>> getUserPlantsByUserId(@PathVariable Long userId) {
        List<UserPlantSimpleDto> userPlantDtoList = userPlantService.getUserPlantsByUserIdWithWaterRequired(userId);
        return new ResponseEntity<>(userPlantDtoList, HttpStatus.OK);
    }

    /**
     * 사용자가 가지고 있는 특정 식물 정보 조회
     *
     * @param userPlantId 조회할 사용자 식물 id
     * @return 사용자가 가지고 있는 특정 식물 정보
     */
    @GetMapping("/{userPlantId}")
    public ResponseEntity<UserPlantSimpleDto> getUserPlantById(@PathVariable Long userPlantId) {
        UserPlantSimpleDto userPlantDto = userPlantService.getSimpleUserPlantById(userPlantId);
        return new ResponseEntity<>(userPlantDto, HttpStatus.OK);
    }

    /**
     * 사용자가 가지고 있는 식물 정보 추가
     *
     * @param userPlantDto 추가할 사용자 식물 정보
     * @return 추가된 사용자 식물 정보
     */
    @PostMapping("")
    public ResponseEntity<UserPlantSimpleDto> addUserPlant(@RequestBody @Valid UserPlantDto userPlantDto) {
        UserPlantSimpleDto addedUserPlantDto = userPlantService.addUserPlant(userPlantDto);
        return new ResponseEntity<>(addedUserPlantDto, HttpStatus.CREATED);
    }


    /**
     * 사용자가 가지고 있는 특정 식물 정보 수정
     *
     * @param userPlantId  수정할 사용자 식물 id
     * @param userPlantDto 수정할 사용자 식물 정보
     * @return 수정된 사용자 식물 정보
     */
    @PutMapping("/{userPlantId}")
    public ResponseEntity<UserPlantSimpleDto> updateUserPlant(@PathVariable Long userPlantId, @RequestBody UserPlantDto userPlantDto) {
        UserPlantSimpleDto updatedUserPlantDto = userPlantService.updateUserPlant(userPlantId, userPlantDto);
        return new ResponseEntity<>(updatedUserPlantDto, HttpStatus.OK);
    }

    /**
     * 사용자가 가지고 있는 특정 식물 정보 삭제
     *
     * @param userPlantId 삭제할 사용자 식물 id
     */
    @DeleteMapping("/{userPlantId}")
    public ResponseEntity<Void> deleteUserPlant(@PathVariable Long userPlantId) {
        userPlantService.deleteUserPlant(userPlantId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
