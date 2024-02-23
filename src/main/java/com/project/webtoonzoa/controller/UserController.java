package com.project.webtoonzoa.controller;

import com.project.webtoonzoa.dto.CommonResponse;
import com.project.webtoonzoa.dto.UserRequestDto;
import com.project.webtoonzoa.global.util.UserDetailsImpl;
import com.project.webtoonzoa.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<CommonResponse<Long>> createUser(
        @RequestBody UserRequestDto userRequestDto
    ) {
        return ResponseEntity.status(HttpStatus.CREATED.value()).body(
            CommonResponse.<Long>builder()
                .status(HttpStatus.CREATED.value())
                .message("회원가입이 성공하였습니다.")
                .data(userService.createUser(userRequestDto))
                .build()
        );
    }
}
