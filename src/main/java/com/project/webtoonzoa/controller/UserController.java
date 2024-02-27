package com.project.webtoonzoa.controller;

import com.project.webtoonzoa.dto.user.SignUpRequestDto;
import com.project.webtoonzoa.dto.user.UserBannedResponseDto;
import com.project.webtoonzoa.dto.user.UserInfoRequestDto;
import com.project.webtoonzoa.dto.user.UserInfoResponseDto;
import com.project.webtoonzoa.dto.user.UserPasswordRequestDto;
import com.project.webtoonzoa.global.response.CommonResponse;
import com.project.webtoonzoa.global.response.ErrorResponse;
import com.project.webtoonzoa.global.util.UserDetailsImpl;
import com.project.webtoonzoa.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/users/signup")
    public ResponseEntity<CommonResponse<?>> createUser(
        @Valid @RequestBody SignUpRequestDto userRequestDto,
        BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return validateRequestDto(
                bindingResult,
                "회원가입 실패"
            );
        }
        return ResponseEntity.status(HttpStatus.CREATED.value()).body(
            CommonResponse.<Long>builder()
                .status(HttpStatus.CREATED.value())
                .message("회원가입이 성공하였습니다.")
                .data(userService.createUser(userRequestDto))
                .build()
        );
    }

    @PutMapping("/my")
    public ResponseEntity<CommonResponse<?>> updateUser(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @Valid @RequestBody UserInfoRequestDto requestDto,
        BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return validateRequestDto(
                bindingResult,
                "nickName을 넣어주세요"
            );
        }
        return ResponseEntity.status(HttpStatus.OK.value()).body(
            CommonResponse.<UserInfoResponseDto>builder()
                .message("회원정보가 수정되었습니다.")
                .status(HttpStatus.OK.value())
                .data(userService.updateUser(userDetails.getUser(), requestDto))
                .build()
        );
    }

    @PutMapping("/my/password")
    public ResponseEntity<CommonResponse<?>> updateUserPassword(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @Valid @RequestBody UserPasswordRequestDto requestDto,
        BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return validateRequestDto(
                bindingResult,
                "비밀번호를 형식에 맞춰서 기입해주세요"
            );
        }
        userService.updatePassword(requestDto, userDetails.getUser());
        return ResponseEntity.status(HttpStatus.OK.value()).body(
            CommonResponse.<Void>builder()
                .message("비밀번호가 변경되었습니다.")
                .status(HttpStatus.OK.value())
                .build()
        );
    }



    private ResponseEntity<CommonResponse<?>> validateRequestDto(
        BindingResult bindingResult,
        String message
    ) {
        if(bindingResult.hasErrors()){
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            List<ErrorResponse> ErrorResponseList = new ArrayList<>();
            for (FieldError fieldError : fieldErrors) {
                ErrorResponse exceptionResponse = new ErrorResponse(fieldError.getDefaultMessage());
                ErrorResponseList.add(exceptionResponse);
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(
                CommonResponse.<List<ErrorResponse>>builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message(message)
                    .data(ErrorResponseList)
                    .build()
            );
        }
        return null;
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

    @PutMapping("/users/{userId}/bans")
    public ResponseEntity<CommonResponse<UserBannedResponseDto>> banUser(
        @PathVariable Long userId,
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        UserBannedResponseDto userBannedResponseDto = userService.banUser(userId,
            userDetails.getUser());
        return ResponseEntity.status(HttpStatus.OK.value()).body(
            CommonResponse.<UserBannedResponseDto>builder()
                .message(
                    userBannedResponseDto.isBanned() ?
                        "회원이 차단되었습니다."
                        : "회원의 차단이 풀렸습니다"
                )
                .status(HttpStatus.OK.value())
                .data(userBannedResponseDto)
                .build()
        );
    }

}
