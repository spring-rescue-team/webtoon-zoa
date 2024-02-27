package com.project.webtoonzoa.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserBannedResponseDto {
    private Long id;
    private boolean banned;
}
