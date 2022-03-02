package ru.mhelper.models.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class MessageResponse extends AbstractResponse {
    public static final int FINE_CODE = 1;
    public static final int BAD_CODE = -1;
    public static final int BAD_TELEGRAM_CODE = -2;
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
