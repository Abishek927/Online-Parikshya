package com.online.exam.exception;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import java.io.IOException;
@Component

public class JwtAuthenticationEntryPoint extends Http403ForbiddenEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException arg2) throws IOException {
        response.setStatus(HttpStatus.FORBIDDEN.value());
        RequestDispatcher rd=request.getRequestDispatcher("/error");
        try{
            rd.forward(request,response);

        }catch (ServletException | IOException e){
            e.printStackTrace();
        }
    }
}
