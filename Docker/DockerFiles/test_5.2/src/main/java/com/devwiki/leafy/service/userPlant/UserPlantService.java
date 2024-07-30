package com.devwiki.leafy.service.userPlant;

import com.devwiki.leafy.dto.plant.PlantDetailDto;
import com.devwiki.leafy.dto.user.UserDto;
import com.devwiki.leafy.dto.userPlant.UserPlantDto;
import com.devwiki.leafy.dto.userPlant.UserPlantMapper;
import com.devwiki.leafy.dto.userPlant.UserPlantSimpleDto;
import com.devwiki.leafy.exception.ResourceNotFoundException;
import com.devwiki.leafy.model.plantLog.PlantLog;
import com.devwiki.leafy.model.user.User;
import com.devwiki.leafy.model.userPlant.UserPlant;
import com.devwiki.leafy.repository.plantLog.PlantLogRepository;
import com.devwiki.leafy.repository.userPlant.UserPlantRepository;
import com.devwiki.leafy.service.plant.PlantService;
import com.devwiki.leafy.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserPlantService {

    private final UserPlantRepository userPlantRepository;
    private final UserService userService;
    private final PlantService plantService;
    private final PlantLogRepository plantLogRepository;

    /**
     * 특정 사용자가 가지고 있는 모든 식물 정보 조회
     *
     * @param userId 조회할 사용자 id
     * @return 사용자가 가지고 있는 모든 식물 정보
     * @throws ResourceNotFoundException 해당 사용자가 존재하지 않을 경우 예외 발생
     */
    public List<UserPlantSimpleDto> getUserPlantsByUserIdWithWaterRequired(Long userId) {
        UserDto userDto = userService.getUserById(userId);
        List<UserPlant> userPlants = userPlantRepository.findAllByUser(new User(userDto));
        List<UserPlantSimpleDto> userPlantSimpleDtos = new ArrayList<>();

        for (UserPlant userPlant : userPlants) {
            if (userPlant.getPlant() == null) {
                userPlantSimpleDtos.add(UserPlantMapper.toSimpleDto(userPlant));
                continue;
            }
            int waterInterval = userPlant.getPlant().getWateringInterval();
            Optional<LocalDateTime> lastWateredAt = plantLogRepository.findAllByUserPlant_UserPlantId_OrderByLogDateDesc(userPlant.getUserPlantId())
                    .stream().filter(PlantLog::isWatered)
                    .map(PlantLog::getCreatedAt)
                    .findFirst();

            if (lastWateredAt.isPresent()) {
                UserPlantSimpleDto userPlantSimpleDto = UserPlantMapper.toSimpleDto(userPlant);
                userPlantSimpleDto.setWaterRequired(lastWateredAt.get().plusDays(waterInterval).isBefore(LocalDateTime.now()));
                userPlantSimpleDtos.add(userPlantSimpleDto);
            } else {
                UserPlantSimpleDto userPlantSimpleDto = UserPlantMapper.toSimpleDto(userPlant);
                userPlantSimpleDto.setWaterRequired(true);
                userPlantSimpleDtos.add(userPlantSimpleDto);
            }

        }

        return userPlantSimpleDtos;
    }

    /**
     * 사용자가 가지고 있는 특정 식물 정보 조회
     *
     * @param userPlantId 조회할 사용자 식물 id
     * @return 사용자가 가지고 있는 특정 식물 정보
     * @throws ResourceNotFoundException 해당 사용자 식물 정보가 존재하지 않을 경우 예외 발생
     */
    public UserPlantSimpleDto getSimpleUserPlantById(Long userPlantId) {
        UserPlant userPlant = userPlantRepository.findById(userPlantId)
                .orElseThrow(() -> new ResourceNotFoundException("UserPlant", "id", userPlantId));
        return UserPlantMapper.toSimpleDto(userPlant);
    }

    /**
     * 사용자가 가지고 있는 특정 식물 정보 조회
     *
     * @param userPlantId 조회할 사용자 식물 id
     * @return 사용자가 가지고 있는 특정 식물 정보
     * @throws ResourceNotFoundException 해당 사용자 식물 정보가 존재하지 않을 경우 예외 발생
     */
    public UserPlantDto getUserPlantById(Long userPlantId) {
        UserPlant userPlant = userPlantRepository.findById(userPlantId)
                .orElseThrow(() -> new ResourceNotFoundException("UserPlant", "id", userPlantId));
        return UserPlantMapper.toDto(userPlant);
    }

    /**
     * 사용자가 가지고 있는 특정 식물 정보 수정
     *
     * @param userPlantDto 수정할 사용자 식물 정보
     * @return 수정된 사용자 식물 정보
     * @throws ResourceNotFoundException 해당 사용자 식물 정보가 존재하지 않을 경우 예외 발생
     */
    @Transactional
    public UserPlantSimpleDto updateUserPlant(Long userPlantId, UserPlantDto userPlantDto) {
        userPlantDto.setUpdatedAt(LocalDateTime.now());
        UserPlant userPlant = userPlantRepository.findById(userPlantId)
                .orElseThrow(() -> new ResourceNotFoundException("UserPlant", "id", userPlantDto.getUserPlantId()));
        userPlant.updateEntity(userPlantDto);
        userPlantRepository.save(userPlant);
        return UserPlantMapper.toSimpleDto(userPlant);
    }

    /**
     * 사용자가 가지고 있는 특정 식물 정보 삭제
     *
     * @param userPlantId 삭제할 사용자 식물 id
     * @throws ResourceNotFoundException 해당 사용자 식물 정보가 존재하지 않을 경우 예외 발생
     */
    @Transactional
    public void deleteUserPlant(Long userPlantId) {
        UserPlant userPlant = userPlantRepository.findById(userPlantId)
                .orElseThrow(() -> new ResourceNotFoundException("UserPlant", "id", userPlantId));
        userPlantRepository.delete(userPlant);
    }

    /**
     * 사용자가 가지고 있는 식물 정보 추가
     *
     * @param userPlantDto 추가할 사용자 식물 정보
     * @return 추가된 사용자 식물 정보
     * @throws ResourceNotFoundException 해당 사용자 또는 해당 식물 정보가 존재하지 않을 경우 예외 발생
     */
    @Transactional
    public UserPlantSimpleDto addUserPlant(UserPlantDto userPlantDto) {
        //생성 시간
        userPlantDto.setCreatedAt(LocalDateTime.now());
        userPlantDto.setUpdatedAt(LocalDateTime.now());

        //사용자 및 식물 정보 조회 및 추가
        UserDto userDto = userService.getUserById(userPlantDto.getUser().getUserId());
        userPlantDto.setUser(userDto);
        PlantDetailDto plantDetailDto = plantService.getPlantById(userPlantDto.getPlant().getPlantId());
        userPlantDto.setPlant(plantDetailDto);

        //사용자 식물 정보 추가
        UserPlant userPlant = new UserPlant(userPlantDto);
        userPlantRepository.save(userPlant);

        return UserPlantMapper.toSimpleDto(userPlant);
    }
}