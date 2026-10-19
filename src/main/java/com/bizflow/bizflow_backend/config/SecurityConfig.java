package com.bizflow.bizflow_backend.config;

import com.bizflow.bizflow_backend.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(
            JwtAuthenticationFilter jwtAuthenticationFilter
    ) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable)

                // Enable CORS
                .cors(cors -> {})

                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )

                .authorizeHttpRequests(auth -> auth

                        // Allow browser CORS preflight requests
                        .requestMatchers(HttpMethod.OPTIONS, "/**")
                        .permitAll()

                        // Authentication
                        .requestMatchers(
                                "/api/v1/auth/**"
                        ).permitAll()

                        // ADMIN ONLY
                        .requestMatchers(
                                "/api/v1/admin/**"
                        ).hasRole("ADMIN")

                        // USER MANAGEMENT
                        .requestMatchers(
                                "/api/v1/users/**"
                        ).hasAnyRole("ADMIN", "MANAGER")

                        // DEPARTMENTS
                        .requestMatchers(
                                "/api/v1/departments/**"
                        ).hasAnyRole("ADMIN", "MANAGER")

                        // WORKFLOWS
                        .requestMatchers(
                                "/api/v1/workflows/**"
                        ).hasAnyRole("ADMIN", "MANAGER")

                        // TASK CREATION AND ASSIGNMENT
                        .requestMatchers(
                                "/api/v1/tasks/*/assign/**"
                        ).hasAnyRole("ADMIN", "MANAGER")

                        // TASK STATUS
                        .requestMatchers(
                                "/api/v1/tasks/*/status"
                        ).authenticated()

                        // OTHER TASK ENDPOINTS
                        .requestMatchers(
                                "/api/v1/tasks/**"
                        ).authenticated()

                        .anyRequest().authenticated()
                )

                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }


    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration =
                new CorsConfiguration();

        // Allow local development and Netlify-hosted frontend sites.
        configuration.setAllowedOriginPatterns(
                List.of(
                        "http://localhost:4200",
                        "https://*.netlify.app"
                )
        );

        configuration.setAllowedMethods(
                List.of(
                        "GET",
                        "POST",
                        "PUT",
                        "PATCH",
                        "DELETE",
                        "OPTIONS"
                )
        );

        // Allow Authorization and Content-Type headers
        configuration.setAllowedHeaders(
                List.of("*")
        );

        configuration.setExposedHeaders(
                List.of("Authorization")
        );

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration(
                "/**",
                configuration
        );

        return source;
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
