package com.bizflow.bizflow_backend.security;

import com.bizflow.bizflow_backend.entity.User;
import com.bizflow.bizflow_backend.service.JwtService;
import com.bizflow.bizflow_backend.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserService userService;

    private static final Logger logger =
            LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    public JwtAuthenticationFilter(
            JwtService jwtService,
            UserService userService
    ) {
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        logger.info("JWT Filter called for: {}", request.getRequestURI());

        String authHeader =
                request.getHeader("Authorization");

        if (authHeader == null
                || !authHeader.startsWith("Bearer ")) {

            logger.warn("No Bearer token found");

            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        try {

            String email = jwtService.extractEmail(token);

            logger.info("Email extracted from token: {}", email);

            if (email != null
                    && SecurityContextHolder
                    .getContext()
                    .getAuthentication() == null
                    && jwtService.isTokenValid(token, email)) {

                logger.info("Token is valid");

                User user = userService
                        .findByEmailWithRoles(email)
                        .orElse(null);

                if (user == null) {

                    logger.error("User not found for email: {}", email);

                } else if (!user.isActive()) {

                    logger.error("User is inactive: {}", email);

                } else {

                    List<SimpleGrantedAuthority> authorities =
                            user.getRoles()
                                    .stream()
                                    .map(role ->
                                            new SimpleGrantedAuthority(
                                                    "ROLE_" + role.getName()
                                            )
                                    )
                                    .toList();

                    logger.info("Authenticated user: {}", user.getEmail());
                    logger.info("Authorities: {}", authorities);

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    user,
                                    null,
                                    authorities
                            );

                    authentication.setDetails(
                            new WebAuthenticationDetailsSource()
                                    .buildDetails(request)
                    );

                    SecurityContextHolder
                            .getContext()
                            .setAuthentication(authentication);

                    logger.info("Authentication added to SecurityContext");
                }

            } else {

                logger.warn("Token validation failed or authentication already exists");
            }

        } catch (Exception e) {

            logger.error("JWT authentication failed", e);

            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }
}