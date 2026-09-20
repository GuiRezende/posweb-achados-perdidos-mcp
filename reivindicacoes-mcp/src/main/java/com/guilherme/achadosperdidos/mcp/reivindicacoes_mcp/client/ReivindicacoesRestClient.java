package com.guilherme.achadosperdidos.mcp.reivindicacoes_mcp.client;

import com.guilherme.achadosperdidos.mcp.reivindicacoes_mcp.dto.ReivindicacaoDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
@Slf4j
public class ReivindicacoesRestClient {

    private final RestClient restClient;

    public ReivindicacoesRestClient(@Value("${services.reivindicacoes.base-url:http://localhost:8083}") String baseUrl) {
        log.info("[REIVINDICACAO-MCP] Inicializando cliente REST. baseUrl={}", baseUrl);
        this.restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    public List<ReivindicacaoDTO> listarTodas() {
        return restClient.get()
                .uri("/reivindicacoes")
                .retrieve()
                .body(new ParameterizedTypeReference<List<ReivindicacaoDTO>>() {});
    }

    public ReivindicacaoDTO buscarPorId(Long id) {
        return restClient.get()
                .uri("/reivindicacoes/{id}", id)
                .retrieve()
                .body(ReivindicacaoDTO.class);
    }

    public ReivindicacaoDTO solicitar(ReivindicacaoDTO dto) {
        log.info("[REIVINDICACAO-MCP] POST /reivindicacoes iniciado. ocorrenciaId={}, nomeSolicitante={}, emailInformado={}, comprovacaoInformada={}",
                dto.ocorrenciaId(), dto.nomeSolicitante(), dto.email() != null && !dto.email().isBlank(),
                dto.comprovacao() != null && !dto.comprovacao().isBlank());
        try {
            ReivindicacaoDTO resposta = restClient.post()
                    .uri("/reivindicacoes")
                    .body(dto)
                    .retrieve()
                    .body(ReivindicacaoDTO.class);
            log.info("[REIVINDICACAO-MCP] POST /reivindicacoes concluído. id={}, ocorrenciaId={}, status={}",
                    resposta != null ? resposta.id() : null,
                    resposta != null ? resposta.ocorrenciaId() : null,
                    resposta != null ? resposta.status() : null);
            return resposta;
        } catch (RuntimeException ex) {
            log.error("[REIVINDICACAO-MCP] Falha no POST /reivindicacoes. ocorrenciaId={}, tipo={}, mensagem={}",
                    dto.ocorrenciaId(), ex.getClass().getSimpleName(), ex.getMessage(), ex);
            throw ex;
        }
    }
}