package com.project.webtoonzoa.dto.user;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class UserInfoRequestDto {

    @NotNull(message = "nickname이 Null입니다.")
    private String nickname;
}
