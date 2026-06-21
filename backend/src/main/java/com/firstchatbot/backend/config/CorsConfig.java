package com.firstchatbot.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * CORS Configuration for enabling cross-origin requests.
 * This allows the React frontend to communicate with the backend.
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {
    
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // Application is served under context-path '/api' (see application.properties),
        // so allow CORS for the API path. Use a broader pattern to ensure preflight
        // requests are handled correctly when coming from the dev frontend.
        registry.addMapping("/api/**")
                .allowedOrigins(
                                        "http://localhost:3000",
                                        "http://localhost:5173",
                                        "http://localhost:5174",
                                        "http://localhost:5175"
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }
}

