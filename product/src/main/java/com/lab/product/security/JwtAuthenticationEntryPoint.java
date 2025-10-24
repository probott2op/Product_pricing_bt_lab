package com.lab.product.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Custom Security Exception Handlers for JWT Authentication
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, 
                        HttpServletResponse response,
                        AuthenticationException authException) throws IOException, ServletException {
        
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(String.format(
            "{\"error\": \"Unauthorized\", \"message\": \"%s\", \"path\": \"%s\", \"timestamp\": %d}",
            authException.getMessage(),
            request.getRequestURI(),
            System.currentTimeMillis()
        ));
    }
}

@Component("jwtAccessDeniedHandler")
class JwtAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request,
                      HttpServletResponse response,
                      AccessDeniedException accessDeniedException) throws IOException, ServletException {
        
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.getWriter().write(String.format(
            "{\"error\": \"Forbidden\", \"message\": \"Access denied. Admin privileges required.\", \"path\": \"%s\", \"timestamp\": %d}",
            request.getRequestURI(),
            System.currentTimeMillis()
        ));
    }
}
