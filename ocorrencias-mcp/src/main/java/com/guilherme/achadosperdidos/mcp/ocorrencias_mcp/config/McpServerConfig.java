package com.guilherme.achadosperdidos.mcp.ocorrencias_mcp.config;

import com.guilherme.achadosperdidos.mcp.ocorrencias_mcp.tools.OcorrenciasMcpTools;
import io.modelcontextprotocol.server.McpSyncServer;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class McpServerConfig {

    @Bean
    public ToolCallbackProvider objetosToolCallbackProvider(OcorrenciasMcpTools ocorrenciasMcpTools) {
        return MethodToolCallbackProvider.builder()
                .toolObjects(ocorrenciasMcpTools)
                .build();
    }

    @Bean
    CommandLineRunner checkMcpServer(McpSyncServer server) {
        return args -> System.out.println("MCP SERVER OK: " + server);
    }
}