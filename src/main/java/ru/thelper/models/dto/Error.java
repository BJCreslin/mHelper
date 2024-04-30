package ru.thelper.models.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Error {

    private int code;

    private String message;

    private String cause;
}
