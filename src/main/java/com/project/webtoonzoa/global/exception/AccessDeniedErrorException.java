package com.project.webtoonzoa.global.exception;

import java.nio.file.AccessDeniedException;

public class AccessDeniedErrorException extends AccessDeniedException {

    public AccessDeniedErrorException(String message) {
        super(message);
    }
}