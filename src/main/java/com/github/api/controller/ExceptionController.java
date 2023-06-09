package com.github.api.controller;

import com.github.api.exception.NoSuchUserExistsException;
import com.github.api.exception.UnsupportedHeaderException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.github.api.exception.ErrorResponse;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@ResponseBody
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ExceptionController extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = NoSuchUserExistsException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNoSuchUserExistsException(NoSuchUserExistsException e){
        return new ErrorResponse(HttpStatus.NOT_FOUND.value(), e.getMessage());
    }


    @ExceptionHandler(value = UnsupportedHeaderException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public ErrorResponse handleUnsupportedHeaderException(UnsupportedHeaderException e) {
        return new ErrorResponse(HttpStatus.NOT_ACCEPTABLE.value(), e.getMessage());
    }

}
