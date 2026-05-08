package com.portfolio.auth.config;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

@Configuration
@Slf4j
public class EnvConfig {

    @PostConstruct
    public void loadEnv() {
        String envFile = ".env";
        try (BufferedReader reader = new BufferedReader(new FileReader(envFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                // Skip comments and empty lines
                if (line.isEmpty() || line.startsWith("#")) continue;

                int index = line.indexOf('=');
                if (index > 0) {
                    String key = line.substring(0, index).trim();
                    String value = line.substring(index + 1).trim();
                    // Only set if not already set by system/IDE
                    if (System.getProperty(key) == null && System.getenv(key) == null) {
                        System.setProperty(key, value);
                        log.debug("Loaded env var: {}", key);
                    }
                }
            }
            log.info(".env file loaded successfully");
        } catch (IOException e) {
            log.warn(".env file not found at '{}' — relying on system environment variables", envFile);
        }
    }
}