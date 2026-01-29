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

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil util;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String authHead = request.getHeader("Authentication");

        if(authHead != null && authHead.startsWith("Bearer")){
            String token = authHead.substring(7);

            try {
                Claims claims = util.extractAllClaims(token);

                UUID userId = UUID.fromString(claims.getSubject());
                List<String> roles = claims.get("roles", List.class);

                List<SimpleGrantedAuthority> auth =
                        roles.stream()
                                .map(SimpleGrantedAuthority::new)
                                .toList();

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userId,null,auth);
                SecurityContextHolder.getContext().setAuthentication(authentication);


            } catch (JwtException e) {

            }


        }

        filterChain.doFilter(request,response);

    }


}
