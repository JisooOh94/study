package com.devwiki.leafy.model.user;

import com.devwiki.leafy.dto.user.UserDto;
import com.devwiki.leafy.dto.user.UserRequestDto;
import com.devwiki.leafy.dto.user.UserResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
@Getter
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "gender")
    private String gender;

    @Column(name = "birthDate")
    private LocalDate birthDate;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public void updateEntity(UserRequestDto userRequestDto) {
        this.name = userRequestDto.getName();
        this.password = userRequestDto.getPassword();
        this.gender = userRequestDto.getGender();
        this.birthDate = userRequestDto.getBirthDate();
        this.updatedAt = LocalDateTime.now();
    }

    public User(UserDto userDto) {
        this.userId = userDto.getUserId();
        this.name = userDto.getName();
        this.email = userDto.getEmail();
        this.password = userDto.getPassword();
        this.gender = userDto.getGender();
        this.birthDate = userDto.getBirthDate();
        this.createdAt = userDto.getCreatedAt();
        this.updatedAt = userDto.getUpdatedAt();
    }

    public User(UserResponseDto userResponseDto) {
        this.userId = userResponseDto.getUserId();
        this.name = userResponseDto.getName();
        this.email = userResponseDto.getEmail();
        this.password = userResponseDto.getPassword();
        this.gender = userResponseDto.getGender();
        this.birthDate = userResponseDto.getBirthDate();
        this.createdAt = userResponseDto.getCreatedAt();
        this.updatedAt = userResponseDto.getUpdatedAt();
    }

    public User(UserRequestDto createDto) {
        this.name = createDto.getName();
        this.email = createDto.getEmail();
        this.password = createDto.getPassword();
        this.gender = createDto.getGender();
        this.birthDate = createDto.getBirthDate();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
}
