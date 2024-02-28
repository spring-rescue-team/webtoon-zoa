package com.project.webtoonzoa.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class UserRequestDto {

    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    @Pattern(regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$", message = "이메일 주소 형식을 지켜주세요")
    private String email;

    @NotBlank(message = "사용할 이름을 입력하세요.")
    private String nickname;

    @NotBlank(message = "비밀번호를 입력하세요.")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,20}$", message = "비밀번호는 8글자~20자, 대문자 1개, 소문자 1개, 숫자 1개, 특수문자 1개 이상 포함하세요.")
    private String password;

    private String imagePath;

    private String adminToken;
}
