package com.example.gnews.mcp;

import com.example.gnews.client.GNewsApiClient;
import io.modelcontextprotocol.json.McpJsonDefaults;
import io.modelcontextprotocol.server.McpServer;
import io.modelcontextprotocol.server.McpSyncServer;
import io.modelcontextprotocol.server.transport.StdioServerTransportProvider;
import io.modelcontextprotocol.spec.McpSchema;

public final class GNewsMcpServer {
    private final GNewsApiClient apiClient;

    public GNewsMcpServer(GNewsApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public McpSyncServer start() {
        StdioServerTransportProvider transport =
                new StdioServerTransportProvider(McpJsonDefaults.getMapper());

        return McpServer.sync(transport)
                .serverInfo("gnews-mcp-server", "1.0.0")
                .capabilities(McpSchema.ServerCapabilities.builder().tools(true).build())
                .toolCall(SearchNewsTool.definition(), (exchange, request) ->
                        SearchNewsTool.handle(apiClient, request.arguments()))
                .toolCall(TopHeadlinesTool.definition(), (exchange, request) ->
                        TopHeadlinesTool.handle(apiClient, request.arguments()))
                .build();
    }
}
