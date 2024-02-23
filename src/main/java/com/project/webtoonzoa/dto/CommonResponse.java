package com.project.webtoonzoa.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommonResponse<T> {

    private String message;
    private int status;
    private T data;
}
