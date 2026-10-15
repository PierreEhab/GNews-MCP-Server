package com.example.gnews;

import com.example.gnews.client.GNewsApiClient;
import com.example.gnews.config.GNewsConfig;
import com.example.gnews.mcp.GNewsMcpServer;
import io.modelcontextprotocol.server.McpSyncServer;

public final class Main {
    private Main() {
    }

    public static void main(String[] args) {
        GNewsConfig config = GNewsConfig.fromEnvironment();
        GNewsApiClient apiClient = new GNewsApiClient(config.apiKey());
        McpSyncServer server = new GNewsMcpServer(apiClient).start();
        Runtime.getRuntime().addShutdownHook(new Thread(server::close));
    }
}
