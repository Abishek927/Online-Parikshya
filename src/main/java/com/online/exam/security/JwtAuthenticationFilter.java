package com.online.exam.security;

import com.online.exam.config.CustomUserDetailService;
import com.online.exam.config.CustomUserDetails;
import com.online.exam.exception.JwtException;
import com.online.exam.jwthelper.JwtTokenHelper;
import com.online.exam.model.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Service
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private CustomUserDetailService customUserDetailService;
@Autowired
    private JwtTokenHelper jwtTokenHelper;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
                    final String  requestHeaderToken=request.getHeader("Authorization");
                    String username=null;
                    String token=null;

                    if(requestHeaderToken!=null && requestHeaderToken.startsWith("Bearer ")){
                         token=requestHeaderToken.substring(7);
                         username=this.jwtTokenHelper.getUsernameFromToken(token);

                         if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null){

                             User user = (User) this.customUserDetailService.loadUserByUsername(username);
                             CustomUserDetails customUserDetails = new CustomUserDetails(user);
                             if(this.jwtTokenHelper.validateToken(token,customUserDetails)) {

                                 UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
                                 usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                                 SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                             }else{
                                 throw new JwtException("token validation failed!!!");
                             }

                         }else{
                             throw new JwtException("username is null or security context holder is not null");
                         }



                    }else{
                        throw new JwtException("Token is not in valid format!!!!");
                    }
                    filterChain.doFilter(request,response);

    }
}
