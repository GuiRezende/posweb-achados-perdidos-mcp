package com.guilherme.achadosperdidos.mcp.reivindicacoes_mcp.config;

import io.modelcontextprotocol.server.McpSyncServer;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class McpServerConfig {

    @Bean
    CommandLineRunner checkMcpServer(McpSyncServer server) {
        return args -> System.out.println("MCP SERVER OK: " + server);
    }
}