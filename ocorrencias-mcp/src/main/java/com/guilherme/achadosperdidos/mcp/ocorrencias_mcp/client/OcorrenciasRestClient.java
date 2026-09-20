package com.guilherme.achadosperdidos.mcp.ocorrencias_mcp.client;

import com.guilherme.achadosperdidos.mcp.ocorrencias_mcp.dto.OcorrenciaDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
public class OcorrenciasRestClient {

    private final RestClient restClient;

    public OcorrenciasRestClient(@Value("${services.ocorrencias.base-url:http://localhost:8082}") String baseUrl) {
        this.restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    public List<OcorrenciaDTO> listarTodas() {
        return restClient.get()
                .uri("/ocorrencias")
                .retrieve()
                .body(new ParameterizedTypeReference<List<OcorrenciaDTO>>() {});
    }

    public OcorrenciaDTO buscarPorId(Long id) {
        return restClient.get()
                .uri("/ocorrencias/{id}", id)
                .retrieve()
                .body(OcorrenciaDTO.class);
    }

    public OcorrenciaDTO buscarPorObjetoId(Long objetoId) {
        return restClient.get()
                .uri("/ocorrencias/objeto/{id}", objetoId)
                .retrieve()
                .body(OcorrenciaDTO.class);
    }

    public OcorrenciaDTO registrar(OcorrenciaDTO dto) {
        return restClient.post()
                .uri("/ocorrencias")
                .body(dto)
                .retrieve()
                .body(OcorrenciaDTO.class);
    }
}