package com.online.exam.filter;

import com.online.exam.helper.JwtHelper;
import com.online.exam.security.CustomUserDetailService;
import com.online.exam.security.UserPrincipal;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.CachingUserDetailsService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
@Service
public class JwtTokenAuthorizationFilter extends OncePerRequestFilter {
    @Autowired
    private JwtHelper jwtHelper;
    @Autowired
    private CustomUserDetailService customUserDetailService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getMethod().equalsIgnoreCase("OPTIONS")) {
            response.setStatus(HttpStatus.OK.value());
        } else {
            String requestToken = request.getHeader("Authorization");
            String userName = null;
            String jwtToken = null;
            if (requestToken != null && requestToken.startsWith("Bearer ")) {
                jwtToken = requestToken.substring(7);
                try {
                    userName = this.jwtHelper.getUserNameFromToken(jwtToken);
                } catch (ExpiredJwtException e) {
                    e.printStackTrace();
                }

            } else {
                System.out.println("Invalid token,not start with bearer string");

            }
            if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.customUserDetailService.loadUserByUsername(userName);
                if (userDetails != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    List<GrantedAuthority> authorities = this.jwtHelper.getAuthoritiesClaimsFromToken(jwtToken);
                    Authentication authentication = this.jwtHelper.getAuthentication(userName, authorities, request);
                    SecurityContextHolder.getContext().setAuthentication(authentication);


                }

            } else {
                SecurityContextHolder.clearContext();
            }


            filterChain.doFilter(request, response);
        }
    }


}
