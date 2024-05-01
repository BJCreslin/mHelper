package ru.thelper.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.thelper.services.exceptions.BadDataParsingException;

@CrossOrigin
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException {

    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException(String errorFromParsing, BadDataParsingException exception) {
        super(errorFromParsing, exception);
    }
}
