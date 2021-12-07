package ru.zhelper.zhelper.controllers.exeptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.zhelper.zhelper.services.exceptions.BadDataParsingException;

@CrossOrigin
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException {
    public static final String ROLE_NOT_FOUND = "Role not found";
    public static final String USER_NOT_FOUND = "User not found";

    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException(String errorFromParsing, BadDataParsingException exception) {
        super(errorFromParsing, exception);
    }
}
