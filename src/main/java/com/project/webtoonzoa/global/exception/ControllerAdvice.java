package com.project.webtoonzoa.global.exception;

import com.project.webtoonzoa.dto.CommonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j(topic = "Exception")
@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(PasswordNotEqualException.class)
    public ResponseEntity<CommonResponse<String>> handleValidationException(
        PasswordNotEqualException e) {
        log.error("회원 비밀번호 불일치 에러", e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            CommonResponse.<String>builder()
                .message(e.getMessage())
                .status(HttpStatus.BAD_REQUEST.value())
                .build()
        );
    }

    @ExceptionHandler(PasswordNotConfirmException.class)
    public ResponseEntity<CommonResponse<String>> handleValidationException(
        PasswordNotConfirmException e) {
        log.error("변경 비밀번호 불일치 에러", e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            CommonResponse.<String>builder()
                .message(e.getMessage())
                .status(HttpStatus.BAD_REQUEST.value())
                .build()
        );
    }
}
