package com.elastiSearchClient.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class CustomExceptionHandler {
    @ExceptionHandler(value = {InvalidInputException.class})
    public ResponseEntity<String> inValidResponse(InvalidInputException ex , WebRequest request){
        String errorMessage = ex.getLocalizedMessage();
        System.out.println(errorMessage);
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }
}
