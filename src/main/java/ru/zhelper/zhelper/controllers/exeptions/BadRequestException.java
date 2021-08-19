package ru.zhelper.zhelper.controllers.exeptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.zhelper.zhelper.services.exceptions.BadDataParsingException;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException {
    public BadRequestException(String errorFromParsing, BadDataParsingException exception) {
        super(errorFromParsing, exception);
    }
}
