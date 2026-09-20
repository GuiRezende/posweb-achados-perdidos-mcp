package com.guilherme.achadosperdidos.mcp.reivindicacoes_mcp.tools;

import com.guilherme.achadosperdidos.mcp.reivindicacoes_mcp.client.ReivindicacoesRestClient;
import com.guilherme.achadosperdidos.mcp.reivindicacoes_mcp.dto.ReivindicacaoDTO;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ReivindicacoesMcpTools {

    private final ReivindicacoesRestClient restClient;

    public ReivindicacoesMcpTools(ReivindicacoesRestClient restClient) {
        this.restClient = restClient;
    }

    @Tool(description = "Lista todas as solicitações de reivindicação de objetos feitas no IFBA.")
    public List<ReivindicacaoDTO> listarReivindicacoes() {
        return restClient.listarTodas();
    }

    @Tool(description = "Busca os detalhes de uma reivindicação específica pelo seu ID.")
    public ReivindicacaoDTO buscarReivindicacaoPorId(@ToolParam(description = "ID da reivindicação") Long id) {
        return restClient.buscarPorId(id);
    }

    @Tool(description = "Abre uma nova solicitação de reivindicação de um objeto achado no IFBA.")
    public ReivindicacaoDTO solicitarReivindicacao(
            @ToolParam(description = "ID do objeto que se deseja reivindicar") Long objetoId,
            @ToolParam(description = "Matrícula do aluno ou servidor solicitante") String matricula,
            @ToolParam(description = "Justificativa ou comprovação de posse do objeto") String justificativa,
            @ToolParam(description = "Data e hora da solicitação (ex: 2026-09-19T16:00:00)") String dataHora) {

        ReivindicacaoDTO dto = new ReivindicacaoDTO(null, objetoId, matricula, justificativa, "PENDENTE", dataHora);
        return restClient.solicitar(dto);
    }
}