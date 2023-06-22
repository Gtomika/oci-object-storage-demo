package com.gaspar.oci.objectstoragedemo.controller;

import com.gaspar.oci.objectstoragedemo.dto.ErrorResponse;
import com.gaspar.oci.objectstoragedemo.service.FileNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.http.HttpStatus;

@Slf4j
@RestControllerAdvice
public class FileControllerAdvice {

    @ExceptionHandler(FileNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleFileNotFound(FileNotFoundException e) {
        return new ErrorResponse("Failed to find the file with name: " + e.getFileName());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleInternalError(Exception e) {
        log.error("An unexpected error occurred!", e);
        return new ErrorResponse("Sorry, internal error!");
    }

}
