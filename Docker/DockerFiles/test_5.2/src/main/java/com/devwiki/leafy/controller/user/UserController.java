package com.devwiki.leafy.controller.user;

import com.devwiki.leafy.dto.user.LoginDto;
import com.devwiki.leafy.dto.user.UserPutRequestDto;
import com.devwiki.leafy.dto.user.UserRequestDto;
import com.devwiki.leafy.dto.user.UserResponseDto;
import com.devwiki.leafy.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 모든 사용자 조회
     *
     * @return 모든 사용자 리스트
     */
    @GetMapping("")
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        List<UserResponseDto> userResponseDtoList = userService.getAllUsers();
        return new ResponseEntity<>(userResponseDtoList, HttpStatus.OK);
    }

    /**
     * 사용자 ID로 조회
     *
     * @param userId 조회할 사용자 ID
     * @return 조회된 사용자
     */
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long userId) {
        UserResponseDto userResponseDto = userService.getUserResponseById(userId);
        return new ResponseEntity<>(userResponseDto, HttpStatus.OK);
    }

    /**
     * 새로운 사용자 추가
     *
     * @param userRequestDto 추가할 사용자 정보
     * @return 추가된 사용자 정보
     */
    @PostMapping("")
    public ResponseEntity<UserResponseDto> addUser(@Valid @RequestBody UserRequestDto userRequestDto) {
        UserResponseDto addedUserResponseDto = userService.createUser(userRequestDto);
        return new ResponseEntity<>(addedUserResponseDto, HttpStatus.CREATED);
    }

    /**
     * 사용자 정보 업데이트
     *
     * @param userPutRequestDto 업데이트할 사용자 정보
     * @return 업데이트된 사용자 정보
     */
    @PutMapping("/{userId}")
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable Long userId, @Valid @RequestBody UserPutRequestDto userPutRequestDto) {
        UserResponseDto updatedUserResponseDto = userService.updateUser(userId, userPutRequestDto);
        return new ResponseEntity<>(updatedUserResponseDto, HttpStatus.OK);
    }

    /**
     * 사용자 정보 삭제
     *
     * @param userId 삭제할 사용자 ID
     */
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

//    /**
//     * 현재 인증된 사용자 정보 조회
//     *
//     * @return 인증된 사용자 정보
//     */
//    @GetMapping("/current")
//    public ResponseEntity<UserResponseDto> getCurrentUser() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//        UserResponseDto userResponseDto = userService.getUserByEmailAndPassword(userDetails.getUsername(), userDetails.getPassword());
//        return new ResponseEntity<>(userResponseDto, HttpStatus.OK);
//    }

    /**
     * 사용자 로그인
     *
     * @param loginDto 로그인 정보
     * @return 로그인한 사용자 정보
     */
    @PostMapping("/login")
    public ResponseEntity<UserResponseDto> login(@RequestBody LoginDto loginDto) {
        UserResponseDto userResponseDto = userService.getUserByEmailAndPassword(loginDto.getEmail(), loginDto.getPassword());
        if (userResponseDto.getUserId() != null) {
            return new ResponseEntity<>(userResponseDto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

}
