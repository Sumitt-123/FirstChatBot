package com.firstchatbot.backend;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Main Spring Boot Application for FirstChatBot Backend
 *
 * This application provides RESTful APIs for chatbot functionality
 * integrated with a free LLM (Language Model).
 */
@SpringBootApplication
@ComponentScan(basePackages = "com.firstchatbot.backend")
public class FirstChatBotBackendApplication {

    public static void main(String[] args) {
        // Load .env file (if present) to populate environment properties
        try {
            Dotenv dotenv = Dotenv.configure()
                    .ignoreIfMalformed()
                    .ignoreIfMissing()
                    .load();

            String token = dotenv.get("HUGGINGFACE_API_TOKEN");
            if (token != null && !token.isBlank()) {
                // Set as a system property so Spring can resolve ${HUGGINGFACE_API_TOKEN}
                System.setProperty("HUGGINGFACE_API_TOKEN", token);
                System.out.println("Loaded HUGGINGFACE_API_TOKEN from .env");
            } else {
                System.out.println("No HUGGINGFACE_API_TOKEN found in .env; relying on environment variables");
            }
        } catch (Exception e) {
            System.out.println("Failed to load .env file: " + e.getMessage());
        }

        SpringApplication.run(FirstChatBotBackendApplication.class, args);
    }

}

