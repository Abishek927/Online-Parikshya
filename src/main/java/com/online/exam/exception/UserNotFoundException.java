package com.online.exam.exception;

import org.springframework.stereotype.Component;


public class UserNotFoundException extends RuntimeException {


    private String message;
    private String fieldName;

    public UserNotFoundException(String message,String fieldName){
        super(String.format("%s with username %s!!!",message,fieldName));
        this.message = message;
        this.fieldName = fieldName;
    }
}
