package com.online.exam.exception;
public class JwtException extends RuntimeException{
    private String message;

    public JwtException(String message) {
        this.message = message;
    }
}
