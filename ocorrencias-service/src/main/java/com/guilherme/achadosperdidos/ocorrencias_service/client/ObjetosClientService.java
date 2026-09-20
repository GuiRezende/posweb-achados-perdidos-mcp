package com.guilherme.achadosperdidos.ocorrencias_service.client;


import com.guilherme.achadosperdidos.ocorrencias_service.dto.ObjetoDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
public class ObjetosClientService {

    private final RestClient objetosRestClient;

    public ObjetoDTO buscarObjetoPorId(Long objetoId) {
        try {
            return objetosRestClient.get()
                    .uri("/objetos/{id}", objetoId)
                    .retrieve()
                    .body(ObjetoDTO.class);
        } catch (HttpClientErrorException.NotFound e) {
            throw new IllegalArgumentException("Objeto não encontrado com id: " + objetoId);
        }
    }
}
