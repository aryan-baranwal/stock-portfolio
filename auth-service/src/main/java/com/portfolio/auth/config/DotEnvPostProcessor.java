package com.portfolio.auth.config;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DotEnvPostProcessor implements EnvironmentPostProcessor {

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment,
                                       SpringApplication application) {
        Map<String, Object> props = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(".env"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;
                int index = line.indexOf('=');
                if (index > 0) {
                    String key = line.substring(0, index).trim();
                    String value = line.substring(index + 1).trim();
                    props.put(key, value);
                }
            }
            environment.getPropertySources()
                    .addFirst(new MapPropertySource("dotenv", props));
        } catch (IOException e) {
            // .env not found — fine in production
        }
    }
}