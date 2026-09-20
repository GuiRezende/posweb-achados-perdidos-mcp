package com.guilherme.achadosperdidos.mcp.reivindicacoes_mcp.tools;

import com.guilherme.achadosperdidos.mcp.reivindicacoes_mcp.client.ReivindicacoesRestClient;
import com.guilherme.achadosperdidos.mcp.reivindicacoes_mcp.dto.ReivindicacaoDTO;
import org.springframework.ai.mcp.annotation.McpTool;
import org.springframework.ai.mcp.annotation.McpToolParam;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Component
@Slf4j
public class ReivindicacoesMcpTools {

    private final ReivindicacoesRestClient restClient;

    public ReivindicacoesMcpTools(ReivindicacoesRestClient restClient) {
        this.restClient = restClient;
    }

    @McpTool(description = "Lista todas as solicitações de reivindicação de objetos feitas no IFBA.")
    public List<ReivindicacaoDTO> listarReivindicacoes() {
        return restClient.listarTodas();
    }

    @McpTool(description = "Busca os detalhes de uma reivindicação específica pelo seu ID.")
    public ReivindicacaoDTO buscarReivindicacaoPorId(@McpToolParam(description = "ID da reivindicação") Long id) {
        return restClient.buscarPorId(id);
    }

    @McpTool(name = "criar_reivindicacao", description = """
            Abre uma nova solicitação de reivindicação de uma ocorrência de objeto achado no IFBA.
            O ocorrenciaId deve ser o campo id retornado por registrarOcorrencia ou listarOcorrencias.
            Não use o objetoId neste parâmetro.
            """)
    public ReivindicacaoDTO registrarReivindicacao(
            @McpToolParam(description = "ID da ocorrência do objeto que se deseja reivindicar") Long ocorrenciaId,
            @McpToolParam(description = "Nome completo do solicitante") String nomeSolicitante,
            @McpToolParam(description = "E-mail do solicitante") String email,
            @McpToolParam(description = "Comprovação ou justificativa de posse do objeto") String comprovacao) {

        log.info("[REIVINDICACAO-MCP] Ferramenta registrarReivindicacao chamada. ocorrenciaId={}, nomeSolicitante={}, emailInformado={}, comprovacaoInformada={}",
                ocorrenciaId, nomeSolicitante, email != null && !email.isBlank(),
                comprovacao != null && !comprovacao.isBlank());
        if (ocorrenciaId == null || nomeSolicitante == null || nomeSolicitante.isBlank()
                || email == null || email.isBlank() || comprovacao == null || comprovacao.isBlank()) {
            log.warn("[REIVINDICACAO-MCP] Validação local rejeitou a solicitação. ocorrenciaId={}, nomeInformado={}, emailInformado={}, comprovacaoInformada={}",
                    ocorrenciaId, nomeSolicitante != null && !nomeSolicitante.isBlank(),
                    email != null && !email.isBlank(), comprovacao != null && !comprovacao.isBlank());
            throw new IllegalArgumentException(
                    "Ocorrência, nome, e-mail e comprovação são obrigatórios para solicitar uma reivindicação.");
        }

        ReivindicacaoDTO dto = new ReivindicacaoDTO(
                null, ocorrenciaId, nomeSolicitante, email, comprovacao,
                null, null, null);
        log.debug("[REIVINDICACAO-MCP] DTO montado para envio. ocorrenciaId={}", dto.ocorrenciaId());
        return restClient.solicitar(dto);
    }
}