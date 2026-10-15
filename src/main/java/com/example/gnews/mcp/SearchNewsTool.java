package com.example.gnews.mcp;

import com.example.gnews.client.GNewsApiClient;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import io.modelcontextprotocol.spec.McpSchema.Tool;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

final class SearchNewsTool {
    private SearchNewsTool() {
    }

    static Tool definition() {
        Map<String, Object> properties = new LinkedHashMap<>();
        properties.put("q", GNewsToolSupport.stringProperty(
                "Search query (supports GNews Boolean query syntax).", 1, 200));
        properties.put("lang", GNewsToolSupport.stringProperty("Two-letter language code, such as en.", 2, 2));
        properties.put("country", GNewsToolSupport.stringProperty("Two-letter source country code, such as us.", 2, 2));
        properties.put("max", GNewsToolSupport.integerProperty("Number of articles, from 1 to 100.", 1, 100));
        properties.put("in", GNewsToolSupport.stringProperty(
                "Comma-separated article fields: title,description,content.", 1, 32));
        properties.put("nullable", GNewsToolSupport.stringProperty(
                "Comma-separated nullable fields: description,content,image.", 1, 32));
        properties.put("from", GNewsToolSupport.stringProperty("Earliest publication time in ISO 8601 format.", 1, 40));
        properties.put("to", GNewsToolSupport.stringProperty("Latest publication time in ISO 8601 format.", 1, 40));
        properties.put("sortby", GNewsToolSupport.enumProperty("Sort by publication time or relevance.",
                List.of("publishedAt", "relevance")));
        properties.put("page", GNewsToolSupport.integerProperty("Page number, starting at 1.", 1, null));
        properties.put("truncate", GNewsToolSupport.enumProperty(
                "Optionally truncate article content.", List.of("content")));
        return GNewsToolSupport.tool("search_news", "Search GNews articles by query and optional filters.",
                properties, List.of("q"));
    }

    static CallToolResult handle(GNewsApiClient client, Map<String, Object> arguments) {
        return GNewsToolSupport.invoke(client, "/search", arguments, true);
    }
}
