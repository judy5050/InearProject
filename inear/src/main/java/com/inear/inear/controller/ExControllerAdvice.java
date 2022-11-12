package com.inear.inear.controller;

import com.inear.inear.exception.AlarmException;
import com.inear.inear.exception.ErrorResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExControllerAdvice {

    @ExceptionHandler
    public ResponseEntity<ErrorResult> alarmExHandler(AlarmException e) {
        ErrorResult errorResult = new ErrorResult("400", e.getMessage());
        return new ResponseEntity(errorResult, HttpStatus.BAD_REQUEST);
    }
}
