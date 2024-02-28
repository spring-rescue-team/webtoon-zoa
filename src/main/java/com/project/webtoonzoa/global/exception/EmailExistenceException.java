package com.project.webtoonzoa.global.exception;

public class EmailExistenceException extends IllegalStateException {

    public EmailExistenceException(String message) {
        super(message);
    }
}
