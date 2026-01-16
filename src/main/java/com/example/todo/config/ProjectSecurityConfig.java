package com.example.todo.config;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationManagerResolver;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.oauth2.server.resource.authentication.JwtIssuerAuthenticationManagerResolver;
import org.springframework.security.web.SecurityFilterChain;

import jakarta.servlet.http.HttpServletRequest;

@Configuration
public class ProjectSecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        Map<String, AuthenticationManager> authenticationManagers = new ConcurrentHashMap<>();

        JwtIssuerAuthenticationManagerResolver authenticationManagerResolver = new JwtIssuerAuthenticationManagerResolver(
                issuer -> {

                    if (issuer != null && issuer.startsWith("http://localhost:8081/realms/")) {
                        return authenticationManagers.computeIfAbsent(issuer, iss -> {

                            JwtDecoder jwtDecoder = JwtDecoders.fromIssuerLocation(iss);
                            return new JwtAuthenticationProvider(jwtDecoder)::authenticate;

                        });
                    }
                    throw new OAuth2AuthenticationException(new OAuth2Error("invalid_issuer", "Unknown Realm", null));
                });

        http.authorizeHttpRequests(requests -> requests
                .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()

                .anyRequest().authenticated())


                .oauth2ResourceServer(oauth -> oauth
                        .authenticationManagerResolver(authenticationManagerResolver));

        return http.build();
    }
}