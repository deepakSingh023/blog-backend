package com.example.blog_backend.filter;


import com.example.blog_backend.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.UUID;


@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil util;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        System.out.println("🔴 FILTER START");

        try {
            String authHead = request.getHeader("Authorization");

            System.out.println("DISPATCH TYPE: " + request.getDispatcherType());
            System.out.println("Processing request: " + request.getMethod() + " " + request.getRequestURI());
            System.out.println("Auth Header: " + authHead);

            if (authHead != null && authHead.startsWith("Bearer ")) {
                String token = authHead.substring(7);

                try {
                    System.out.println("Starting token extraction...");
                    Claims claims = util.extractAllClaims(token);
                    String userId = util.extractUserId(token);
                    System.out.println("Extracted UserId: " + userId);

                    List<?> rawRoles = claims.get("roles", List.class);
                    List<SimpleGrantedAuthority> authorities =
                            rawRoles == null ?
                                    Collections.emptyList() :
                                    rawRoles.stream()
                                            .map(Object::toString)
                                            .map(SimpleGrantedAuthority::new)
                                            .toList();

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    userId,
                                    null,
                                    authorities
                            );

                    SecurityContextHolder
                            .getContext()
                            .setAuthentication(authentication);

                } catch (JwtException e) {
                    System.out.println("JWT ERROR: " + e.getMessage());
                    e.printStackTrace();
                    SecurityContextHolder.clearContext();
                }
            }

            System.out.println("🔴 Before doFilter - async: " + request.isAsyncStarted());
            filterChain.doFilter(request, response);
            System.out.println("🔴 After doFilter - async: " + request.isAsyncStarted());

        } catch (Exception e) {
            System.out.println("🔴🔴🔴 EXCEPTION IN FILTER: " + e.getClass().getName());
            System.out.println("🔴🔴🔴 MESSAGE: " + e.getMessage());
            e.printStackTrace();

            // Send error response
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Filter error: " + e.getMessage());
            return; // Don't continue the chain
        }

        System.out.println("🔴 FILTER END");
    }
}
