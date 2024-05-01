package ru.thelper.controllers;

import jakarta.persistence.PersistenceException;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.thelper.exceptions.BadRequestException;
import ru.thelper.models.dto.Error;
import ru.thelper.services.exceptions.BadDataParsingException;


@ControllerAdvice
public class ControllerHandler extends ResponseEntityExceptionHandler {

    private static final String ERROR_FROM_PARSING = "Error parsing";

    @ExceptionHandler({PersistenceException.class})
    protected ResponseEntity<Object> handlePersistenceEx(PersistenceException ex, WebRequest request) {
        var apiError = Error.builder()
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .cause(ex.getMessage())
                .message("PersistenceException").build();
        return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({DataAccessException.class})
    protected ResponseEntity<Object> handleDataAccessEx(DataAccessException ex, WebRequest request) {
        var apiError = Error.builder()
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .cause(ex.getMessage())
                .message("DataAccessException").build();
        return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({BadRequestException.class})
    protected ResponseEntity<Object> handleBadRequestException(BadRequestException ex, WebRequest request) {
        var apiError = Error.builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .cause(ex.getMessage())
                .message("BadRequestException").build();
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({BadDataParsingException.class})
    protected ResponseEntity<Object> handleBadDataParsingException(BadDataParsingException ex, WebRequest request) {
        var apiError = Error.builder()
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .cause(ex.getMessage())
                .message(ERROR_FROM_PARSING).build();
        return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
