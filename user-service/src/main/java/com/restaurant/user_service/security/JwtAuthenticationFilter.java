package com.restaurant.user_service.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final MyUserDetailService customUserDetailsService;
    private final JwtTokenProvider jwtTokenProvider;

    private Long currentUserID;

    public Long getCurrentUserID (){
        return currentUserID;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        try {

            String bearerToken = request.getHeader("Authorization");

            if (bearerToken != null && bearerToken.startsWith("Bearer ")) {

                String token = bearerToken.substring(7);

                if (!token.isBlank()) {

                    String email = jwtTokenProvider.getUsernameFromToken(token);
                    currentUserID = jwtTokenProvider.getUserIdFromToken(token);

                    if (email != null &&
                            SecurityContextHolder.getContext().getAuthentication() == null) {

                        UserDetails userDetails =
                                customUserDetailsService.loadUserByUsername(email);

                        if (jwtTokenProvider.validateToken(token)) {

                            UsernamePasswordAuthenticationToken authentication =
                                    new UsernamePasswordAuthenticationToken(
                                            userDetails,
                                            null,
                                            userDetails.getAuthorities()
                                    );

                            authentication.setDetails(
                                    new WebAuthenticationDetailsSource()
                                            .buildDetails(request)
                            );

                            SecurityContextHolder.getContext()
                                    .setAuthentication(authentication);
                        }
                    }
                }
            }

        } catch (Exception e) {
            System.out.println("JWT Authentication Error: " + e.getMessage());
        }

        filterChain.doFilter(request, response);
    }
}