package com.online.exam.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

public class ApiErrorController implements ErrorController {
    public String getErrorPath(){
        return "/error";
    }
    @RequestMapping("/error")
    public void globalError(HttpServletRequest request, HttpServletResponse response){
        throw new ResponseStatusException(HttpStatus.valueOf(response.getStatus()));
    }
}
