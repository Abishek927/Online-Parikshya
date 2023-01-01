package com.online.exam.controller;

import com.online.exam.config.CustomUserDetailService;
import com.online.exam.config.CustomUserDetails;
import com.online.exam.exception.JwtException;
import com.online.exam.jwthelper.JwtRequest;
import com.online.exam.jwthelper.JwtResponse;
import com.online.exam.jwthelper.JwtTokenHelper;
import com.online.exam.model.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class JwtController {

    @Autowired
    private JwtTokenHelper jwtTokenHelper;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private CustomUserDetailService customUserDetailService;
    @PostMapping("/generate-token")
    ResponseEntity<JwtResponse> generateToken(@RequestBody JwtRequest jwtRequest){
        try{
            authenticate(jwtRequest.getUsername(), jwtRequest.getPassword());
        }catch (Exception e){
            e.printStackTrace();
            throw new JwtException("user is not found");
        }
        User user= (User) customUserDetailService.loadUserByUsername(jwtRequest.getUsername());
        CustomUserDetails customUserDetails=new CustomUserDetails(user);
        String token=this.jwtTokenHelper.generateToken(customUserDetails);
        return new ResponseEntity<JwtResponse>(new JwtResponse(token), HttpStatusCode.valueOf(200));




    }

    private void authenticate(String username,String password){
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken=new UsernamePasswordAuthenticationToken(username,password);
        this.authenticationManager.authenticate(usernamePasswordAuthenticationToken);

    }
}
