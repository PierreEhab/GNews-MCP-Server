package com.example.gnews.config;

public record GNewsConfig(String apiKey) {
    private static final String API_KEY_ENVIRONMENT_VARIABLE = "GNEWS_API_KEY";

    public GNewsConfig {
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalArgumentException("GNews API key must not be blank.");
        }
    }

    public static GNewsConfig fromEnvironment() {
        String apiKey = System.getenv(API_KEY_ENVIRONMENT_VARIABLE);
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException(
                    "Missing required environment variable: " + API_KEY_ENVIRONMENT_VARIABLE);
        }
        return new GNewsConfig(apiKey);
    }
}
