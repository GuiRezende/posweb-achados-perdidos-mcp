package com.guilherme.achadosperdidos.mcp.objetos_mcp.config;

import com.guilherme.achadosperdidos.mcp.objetos_mcp.tools.ObjetosMcpTools;
import io.modelcontextprotocol.server.McpSyncServer;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class McpServerConfig {

    @Bean
    public ToolCallbackProvider objetosToolCallbackProvider(ObjetosMcpTools objetosMcpTools) {
        return MethodToolCallbackProvider.builder()
                .toolObjects(objetosMcpTools)
                .build();
    }

    @Bean
    CommandLineRunner checkMcpServer(McpSyncServer server) {
        return args -> System.out.println("MCP SERVER OK: " + server);
    }
}