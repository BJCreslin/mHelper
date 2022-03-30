package ru.mhelper.controllers;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.mhelper.controllers.exeptions.BadRequestException;
import ru.mhelper.models.dto.Error;

import javax.persistence.PersistenceException;

@ControllerAdvice
public class ControllerHandler extends ResponseEntityExceptionHandler {

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
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .cause(ex.getMessage())
                .message("BadRequestException").build();
        return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
