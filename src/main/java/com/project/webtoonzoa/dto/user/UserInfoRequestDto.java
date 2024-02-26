package com.project.webtoonzoa.dto.user;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class UserInfoRequestDto {

    @NotNull
    private String nickname;
}
