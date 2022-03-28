package ru.mhelper.controllers;

import org.hibernate.exception.SQLGrammarException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.mhelper.models.dto.Error;

@ControllerAdvice
public class ControllerHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({SQLGrammarException.class})
    protected ResponseEntity<Object> handleSQLGramarEx(SQLGrammarException ex, WebRequest request) {
        var apiError = Error.builder()
            .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
            .cause(ex.getMessage())
            .message("SQLGrammarException").build();
        return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
