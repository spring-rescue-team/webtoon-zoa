package com.project.webtoonzoa.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class UserInfoRequestDto {
    @NotNull
    private String nickname;
}
