package com.guilherme.achadosperdidos.mcp.objetos_mcp.client;

import com.guilherme.achadosperdidos.mcp.objetos_mcp.dto.ObjetoDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
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

    public List<ObjetoDTO> buscarPorAtributos(String nome, String categoria, String cor, String marca, String descricao) {
        StringBuilder uriBuilder = new StringBuilder("/objetos/busca?");

        if (nome != null && !nome.isBlank()) {
            uriBuilder.append("nome=").append(URLEncoder.encode(nome, StandardCharsets.UTF_8)).append("&");
        }
        if (categoria != null && !categoria.isBlank()) {
            uriBuilder.append("categoria=").append(URLEncoder.encode(categoria, StandardCharsets.UTF_8)).append("&");
        }
        if (cor != null && !cor.isBlank()) {
            uriBuilder.append("cor=").append(URLEncoder.encode(cor, StandardCharsets.UTF_8)).append("&");
        }
        if (marca != null && !marca.isBlank()) {
            uriBuilder.append("marca=").append(URLEncoder.encode(marca, StandardCharsets.UTF_8)).append("&");
        }
        if (descricao != null && !descricao.isBlank()) {
            uriBuilder.append("descricao=").append(URLEncoder.encode(descricao, StandardCharsets.UTF_8)).append("&");
        }

        String uri = uriBuilder.toString();
        if (uri.endsWith("?")) {
            uri = "/objetos/busca";
        } else if (uri.endsWith("&")) {
            uri = uri.substring(0, uri.length() - 1);
        }

        return restClient.get()
                .uri(uri)
                .retrieve()
                .body(new ParameterizedTypeReference<List<ObjetoDTO>>() {});
    }

    public ObjetoDTO cadastrarObjeto(ObjetoDTO dto) {
        return restClient.post()
                .uri("/objetos")
                .body(dto)
                .retrieve()
                .body(ObjetoDTO.class);
    }
}