package com.diegonunez.Product_MS.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Component
public class ApiKeyAuthFilter extends OncePerRequestFilter {

    @Value("${security.api-key}")
    private String expectedApiKey;

    private static final String HEADER_NAME = "X-API-KEY";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String apiKey = request.getHeader(HEADER_NAME);

        if(!expectedApiKey.equals(apiKey)){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write("""
            {
              "errors": [
                {
                  "status": "401",
                  "title": "Unauthorized",
                  "detail": "Invalid or missing API Key"
                }
              ]
            }
            """);
        }else{
            filterChain.doFilter(request,response);
        }
    }
}
