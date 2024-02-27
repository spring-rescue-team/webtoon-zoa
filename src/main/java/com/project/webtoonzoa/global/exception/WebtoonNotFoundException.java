package com.project.webtoonzoa.global.exception;

import java.util.NoSuchElementException;

public class WebtoonNotFoundException extends NoSuchElementException {

    public WebtoonNotFoundException(String message) {
        super(message);
    }
}