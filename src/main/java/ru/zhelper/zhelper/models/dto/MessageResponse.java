package ru.zhelper.zhelper.models.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class MessageResponse {
    private int code;
    private String message;
}
