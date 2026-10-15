package com.example.gnews.mcp;

import com.example.gnews.client.GNewsApiClient;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import io.modelcontextprotocol.spec.McpSchema.Tool;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

final class TopHeadlinesTool {
    private TopHeadlinesTool() {
    }

    static Tool definition() {
        Map<String, Object> properties = new LinkedHashMap<>();
        properties.put("category", GNewsToolSupport.enumProperty("Headline category.", List.of(
                "general", "world", "nation", "business", "technology",
                "entertainment", "sports", "science", "health")));
        properties.put("lang", GNewsToolSupport.stringProperty("Two-letter language code, such as en.", 2, 2));
        properties.put("country", GNewsToolSupport.stringProperty("Two-letter country code, such as us.", 2, 2));
        properties.put("max", GNewsToolSupport.integerProperty("Number of articles, from 1 to 100.", 1, 100));
        properties.put("q", GNewsToolSupport.stringProperty("Optional GNews Boolean keyword query.", 1, 200));
        properties.put("nullable", GNewsToolSupport.stringProperty(
                "Comma-separated nullable fields: description,content,image.", 1, 32));
        properties.put("from", GNewsToolSupport.stringProperty("Earliest publication time in ISO 8601 format.", 1, 40));
        properties.put("to", GNewsToolSupport.stringProperty("Latest publication time in ISO 8601 format.", 1, 40));
        properties.put("page", GNewsToolSupport.integerProperty("Page number, starting at 1.", 1, null));
        properties.put("truncate", GNewsToolSupport.enumProperty(
                "Optionally truncate article content.", List.of("content")));
        return GNewsToolSupport.tool("get_top_headlines",
                "Get trending GNews headlines by category and optional filters.", properties, List.of());
    }

    static CallToolResult handle(GNewsApiClient client, Map<String, Object> arguments) {
        return GNewsToolSupport.invoke(client, "/top-headlines", arguments, false);
    }
}
