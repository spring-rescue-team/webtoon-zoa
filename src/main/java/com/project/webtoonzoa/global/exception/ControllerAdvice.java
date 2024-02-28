package com.project.webtoonzoa.global.exception;

import com.project.webtoonzoa.global.response.CommonResponse;
import java.util.NoSuchElementException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
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

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<CommonResponse<String>> handleValidationAccessDeniedException(
        AccessDeniedException e) {
        log.error("접근 권한 불일치 에러", e);

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
            CommonResponse.<String>builder()
                .message(e.getMessage())
                .status(HttpStatus.FORBIDDEN.value())
                .build()
        );
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<CommonResponse<String>> handleNoSuchElementException(
        NoSuchElementException e) {
        log.error("웹툰 탐색 실패 에러", e);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
            CommonResponse.<String>builder()
                .message(e.getMessage())
                .status(HttpStatus.NOT_FOUND.value())
                .build()
        );
    }
}
