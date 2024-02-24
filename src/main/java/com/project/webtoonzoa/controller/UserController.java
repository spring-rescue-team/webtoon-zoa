package com.project.webtoonzoa.controller;

import com.project.webtoonzoa.dto.CommonResponse;
import com.project.webtoonzoa.dto.SignUpRequestDto;
import com.project.webtoonzoa.dto.UserInfoRequestDto;
import com.project.webtoonzoa.dto.UserInfoResponseDto;
import com.project.webtoonzoa.global.util.UserDetailsImpl;
import com.project.webtoonzoa.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/users/signup")
    public ResponseEntity<CommonResponse<Long>> createUser(
        @Valid @RequestBody SignUpRequestDto userRequestDto
    ) {
        return ResponseEntity.status(HttpStatus.CREATED.value()).body(
            CommonResponse.<Long>builder()
                .status(HttpStatus.CREATED.value())
                .message("회원가입이 성공하였습니다.")
                .data(userService.createUser(userRequestDto))
                .build()
        );
    }

    @PutMapping("/my")
    public ResponseEntity<CommonResponse<UserInfoResponseDto>> updateUser(
        @Valid @AuthenticationPrincipal UserDetailsImpl userDetails,
        @RequestBody UserInfoRequestDto requestDto
    ) {
        return ResponseEntity.status(HttpStatus.OK.value()).body(
            CommonResponse.<UserInfoResponseDto>builder()
                .message("회원정보가 수정되었습니다.")
                .status(HttpStatus.OK.value())
                .data(userService.updateUser(userDetails.getUser(), requestDto))
                .build()
        );
    }

    @PostMapping("/users/logout")
    public ResponseEntity<CommonResponse<Long>> logoutUser(
        HttpServletResponse response
    ) {
        userService.logoutUser(response);
        return ResponseEntity.status(HttpStatus.OK.value()).body(CommonResponse.<Long>builder()
            .message("로그아웃 되었습니다.")
            .status(HttpStatus.OK.value())
            .build()
        );
    }

}
