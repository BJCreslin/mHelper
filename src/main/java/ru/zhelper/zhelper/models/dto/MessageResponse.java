package ru.zhelper.zhelper.models.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class MessageResponse {
    public static final int FINE_CODE = 1;
    public static final int BAD_CODE = -1;
    private int code;
    private String message;

    public MessageResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public MessageResponse(String message) {
        this(FINE_CODE, message);
    }
}
