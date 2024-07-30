package com.devwiki.leafy.service.user;

import com.devwiki.leafy.dto.user.*;
import com.devwiki.leafy.exception.ResourceNotFoundException;
import com.devwiki.leafy.model.user.User;
import com.devwiki.leafy.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    /**
     * 모든 사용자 조회
     *
     * @return 모든 사용자 리스트
     */
    public List<UserResponseDto> getAllUsers() {
        List<User> userDtos = userRepository.findAll();
        return userDtos.stream().map(UserMapper::toResponseDto).collect(Collectors.toList());
    }

    /**
     * 사용자 ID로 조회
     *
     * @param userId 조회할 사용자 ID
     * @return 조회된 사용자
     */
    public UserResponseDto getUserResponseById(Long userId) {
        User user = findUserById(userId);
        return UserMapper.toResponseDto(user);
    }

    /**
     * 사용자 ID로 조회, 내부 로직 전용
     *
     * @param userId 조회할 사용자 ID
     * @return 조회된 사용자
     */
    public UserDto getUserById(Long userId) {
        User user = findUserById(userId);
        return UserMapper.toDto(user);
    }


    /**
     * 새로운 사용자를 생성합니다.
     *
     * @param userRequestDto 새로 생성할 사용자 정보
     * @return 생성된 사용자 정보
     */
    public UserResponseDto createUser(UserRequestDto userRequestDto) {
        userRequestDto.setCreatedAt(LocalDateTime.now());
        userRequestDto.setUpdatedAt(LocalDateTime.now());
        userRequestDto.setPassword(passwordEncoder.encode(userRequestDto.getPassword())); // 비밀번호 암호화
        User user = new User(userRequestDto);
        userRepository.save(user);
        return UserMapper.toResponseDto(user);
    }

    /**
     * 이메일과 비밀번호를 받아 사용자 정보를 조회합니다.
     *
     * @param email    사용자 이메일
     * @param password 사용자 비밀번호
     * @return 조회된 사용자 정보
     */
    public UserResponseDto getUserByEmailAndPassword(String email, String password) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (passwordEncoder.matches(password, user.getPassword())) {
                return UserMapper.toResponseDto(user);
            }
        }
        return new UserResponseDto();
    }

    /**
     * 사용자 정보를 업데이트합니다.
     *
     * @param userPutRequestDto 업데이트할 사용자 정보
     * @return 업데이트된 사용자 정보
     */
    public UserResponseDto updateUser(Long userId, UserPutRequestDto userPutRequestDto) {
        UserRequestDto userRequestDto = new UserRequestDto();
        userRequestDto.setName(userPutRequestDto.getName());
        userRequestDto.setUpdatedAt(LocalDateTime.now());
        // 비밀번호 암호화
        if (userPutRequestDto.getPassword() != null) {
            userRequestDto.setPassword(passwordEncoder.encode(userPutRequestDto.getPassword()));
        }

        User user = findUserById(userId);
        userRequestDto.setEmail(user.getEmail());
        userRequestDto.setGender(user.getGender());
        userRequestDto.setBirthDate(user.getBirthDate());
        userRequestDto.setCreatedAt(user.getCreatedAt());

        user.updateEntity(userRequestDto);
        userRepository.save(user);

        return UserMapper.toResponseDto(user);
    }


    /**
     * 사용자 정보를 삭제합니다.
     *
     * @param userId 삭제할 사용자 Id
     */
    public void deleteUser(Long userId) {
        User user = findUserById(userId);
        userRepository.delete(user);
    }

    /**
     * 특정 id를 가진 식물 로그 조회
     */
    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("PlantLog", "id", userId));
    }
}
