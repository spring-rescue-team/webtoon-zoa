package com.project.webtoonzoa.dto.user;

import com.project.webtoonzoa.entity.Enum.UserRoleEnum;
import com.project.webtoonzoa.entity.User;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class UserResponseDto {

    private Long id;
    private String email;
    private String nickname;
    private UserRoleEnum role;
    private boolean banned;
    private String imagePath;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public UserResponseDto(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.nickname = user.getNickname();
        this.role = user.getRole();
        this.banned = user.isBanned();
        this.imagePath = user.getImagePath();
        this.createdAt = user.getCreatedAt();
        this.modifiedAt = user.getModifiedAt();
    }
}
