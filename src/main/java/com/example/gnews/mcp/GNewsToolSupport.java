package com.example.gnews.mcp;

import com.example.gnews.client.GNewsApiClient;
import com.example.gnews.client.GNewsApiException;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import io.modelcontextprotocol.spec.McpSchema.Tool;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

final class GNewsToolSupport {
    private GNewsToolSupport() {
    }

    static CallToolResult invoke(GNewsApiClient client, String endpoint,
                                 Map<String, Object> arguments, boolean search) {
        try {
            String response = client.get(endpoint, queryParameters(arguments, search));
            return CallToolResult.builder()
                    .content(List.of(new McpSchema.TextContent(response)))
                    .build();
        } catch (GNewsApiException e) {
            return toolError(e.getMessage());
        } catch (IOException e) {
            return toolError("Could not reach the GNews API: " + e.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return toolError("The GNews API request was interrupted.");
        } catch (IllegalArgumentException e) {
            return toolError(e.getMessage());
        }
    }

    static Tool tool(String name, String description, Map<String, Object> properties, List<String> required) {
        Map<String, Object> schema = new LinkedHashMap<>();
        schema.put("type", "object");
        schema.put("properties", properties);
        schema.put("required", required);
        schema.put("additionalProperties", false);
        return Tool.builder(name, schema).description(description).build();
    }

    static Map<String, Object> stringProperty(String description, int minLength, int maxLength) {
        Map<String, Object> property = new LinkedHashMap<>();
        property.put("type", "string");
        property.put("description", description);
        property.put("minLength", minLength);
        property.put("maxLength", maxLength);
        return property;
    }

    static Map<String, Object> integerProperty(String description, int minimum, Integer maximum) {
        Map<String, Object> property = new LinkedHashMap<>();
        property.put("type", "integer");
        property.put("description", description);
        property.put("minimum", minimum);
        if (maximum != null) {
            property.put("maximum", maximum);
        }
        return property;
    }

    static Map<String, Object> enumProperty(String description, List<String> values) {
        Map<String, Object> property = new LinkedHashMap<>();
        property.put("type", "string");
        property.put("description", description);
        property.put("enum", values);
        return property;
    }

    private static Map<String, String> queryParameters(Map<String, Object> arguments, boolean search) {
        Map<String, String> parameters = new LinkedHashMap<>();
        if (search) {
            parameters.put("q", requiredString(arguments, "q"));
        }
        for (String name : List.of("category", "lang", "country", "in", "nullable",
                "from", "to", "sortby", "truncate")) {
            Object value = arguments.get(name);
            if (value != null) {
                String text = value.toString();
                if (name.equals("from") || name.equals("to")) {
                    validateDateTime(name, text);
                }
                parameters.put(name, text);
            }
        }
        for (String name : List.of("max", "page")) {
            Object value = arguments.get(name);
            if (value != null) {
                parameters.put(name, value.toString());
            }
        }
        return parameters;
    }

    private static void validateDateTime(String name, String value) {
        try {
            OffsetDateTime.parse(value);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException(name + " must be an ISO 8601 date-time.");
        }
    }

    private static String requiredString(Map<String, Object> arguments, String name) {
        Object value = arguments.get(name);
        if (value instanceof String text && !text.isBlank()) {
            return text;
        }
        throw new IllegalArgumentException("The " + name + " parameter is required.");
    }

    private static CallToolResult toolError(String message) {
        return CallToolResult.builder()
                .content(List.of(new McpSchema.TextContent(message)))
                .isError(true)
                .build();
    }
}
