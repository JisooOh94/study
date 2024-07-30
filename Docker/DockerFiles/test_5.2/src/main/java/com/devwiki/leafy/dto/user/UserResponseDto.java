package com.devwiki.leafy.dto.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class UserResponseDto {

    private Long userId;
    private String name;
    private String email;
    @JsonIgnore
    private String password;
    private String gender;
    @JsonIgnore
    private LocalDate birthDate;
    @JsonIgnore
    private LocalDateTime createdAt;
    @JsonIgnore
    private LocalDateTime updatedAt;
}
