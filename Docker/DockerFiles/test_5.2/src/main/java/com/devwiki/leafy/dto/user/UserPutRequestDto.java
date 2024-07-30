package com.devwiki.leafy.dto.user;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class UserPutRequestDto {

    @NotNull
    private String name;
    @NotNull
    private String password;
}
