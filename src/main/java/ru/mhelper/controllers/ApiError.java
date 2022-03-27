package ru.mhelper.controllers;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
public final class ApiError {

    private final String message;

    private final String debugMessage;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Setter
    private List<String> errors;

    public ApiError(String message, String debugMessage) {
        this.message = message;
        this.debugMessage = debugMessage;
    }
}