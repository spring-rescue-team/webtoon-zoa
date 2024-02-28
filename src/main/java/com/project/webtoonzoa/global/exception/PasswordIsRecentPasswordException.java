package com.project.webtoonzoa.global.exception;

public class PasswordIsRecentPasswordException extends IllegalStateException {

    public PasswordIsRecentPasswordException(String message) {
        super(message);
    }
}
