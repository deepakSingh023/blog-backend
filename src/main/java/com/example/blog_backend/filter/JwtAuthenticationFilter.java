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


        try {
            String authHead = request.getHeader("Authorization");


            if (authHead != null && authHead.startsWith("Bearer ")) {
                String token = authHead.substring(7);

                try {

                    Claims claims = util.extractAllClaims(token);
                    String userId = util.extractUserId(token);

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

                    e.printStackTrace();
                    SecurityContextHolder.clearContext();
                }
            }


            filterChain.doFilter(request, response);


        } catch (Exception e) {
            e.printStackTrace();

            // Send error response
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Filter error: " + e.getMessage());
            return; // Don't continue the chain
        }
    }
}
