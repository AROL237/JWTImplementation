package com.Signing.JWTImplementation.ExceptionHandler;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@ResponseStatus
public class CustomExceptionHandler {

    @ExceptionHandler({Exception.class, UsernameNotFoundException.class})
    public Map<String,String> exceptionHandler(Exception exception){
        return (Map<String, String>) new HashMap<>().put("error_message",exception.getMessage());
    }
}
