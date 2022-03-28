package ru.mhelper.models.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
public class Error {

    private int code;

    private String message;

    private String cause;
}
