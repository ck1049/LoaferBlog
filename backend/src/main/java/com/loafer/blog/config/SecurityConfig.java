package com.loafer.blog.config;

import com.loafer.blog.utils.JwtUtils;
import com.loafer.blog.utils.TokenCache;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    private final JwtUtils jwtUtils;
    private final TokenCache tokenCache;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    public SecurityConfig(JwtUtils jwtUtils, TokenCache tokenCache) {
        this.jwtUtils = jwtUtils;
        this.tokenCache = tokenCache;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        final String prefix = contextPath.endsWith("/")
                ? contextPath.substring(0, contextPath.length() - 1)
                : contextPath;
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(prefix + "/api/auth/**").permitAll()
                        .requestMatchers(prefix + "/api/rsa/**").permitAll()
                        .requestMatchers(prefix + "/api/announcements").permitAll()
                        .requestMatchers(prefix + "/api/announcements/**").permitAll()
                        .requestMatchers(prefix + "/api/posts/**").permitAll()
                        .requestMatchers(prefix + "/api/comments").permitAll()
                        .requestMatchers(prefix + "/api/comments/**").permitAll()
                        .requestMatchers(prefix + "/uploads/**").permitAll()
                        .requestMatchers(prefix + "/api/messages/**").authenticated()
                        .requestMatchers(prefix + "/api/categories/**").permitAll()
                        .requestMatchers(prefix + "/api/tags").permitAll()
                        .requestMatchers(prefix + "/api/tags/**").permitAll()
                        .requestMatchers(prefix + "/api/sensitive-words/**").hasRole("ADMIN")
                        .requestMatchers(prefix + "/api/files/**").authenticated()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(new JwtAuthenticationFilter(jwtUtils, tokenCache), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}