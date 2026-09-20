package com.guilherme.achadosperdidos.mcp.reivindicacoes_mcp.client;

import com.guilherme.achadosperdidos.mcp.reivindicacoes_mcp.dto.ReivindicacaoDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
public class ReivindicacoesRestClient {

    private final RestClient restClient;

    public ReivindicacoesRestClient(@Value("${services.reivindicacoes.base-url:http://localhost:8083}") String baseUrl) {
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
        return restClient.post()
                .uri("/reivindicacoes")
                .body(dto)
                .retrieve()
                .body(ReivindicacaoDTO.class);
    }
}