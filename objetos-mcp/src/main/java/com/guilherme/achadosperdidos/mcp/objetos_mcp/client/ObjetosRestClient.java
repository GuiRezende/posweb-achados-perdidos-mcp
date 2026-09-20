package com.guilherme.achadosperdidos.mcp.objetos_mcp.client;

import com.guilherme.achadosperdidos.mcp.objetos_mcp.dto.ObjetoDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
public class ObjetosRestClient {

    private final RestClient restClient;

    public ObjetosRestClient(@Value("${services.objetos.base-url:http://localhost:8081}") String baseUrl) {
        this.restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    public List<ObjetoDTO> listarObjetos() {
        return restClient.get()
                .uri("/objetos")
                .retrieve()
                .body(new ParameterizedTypeReference<List<ObjetoDTO>>() {});
    }

    public ObjetoDTO buscarPorId(Long id) {
        return restClient.get()
                .uri("/objetos/{id}", id)
                .retrieve()
                .body(ObjetoDTO.class);
    }

    public ObjetoDTO cadastrarObjeto(ObjetoDTO dto) {
        return restClient.post()
                .uri("/objetos")
                .body(dto)
                .retrieve()
                .body(ObjetoDTO.class);
    }
}