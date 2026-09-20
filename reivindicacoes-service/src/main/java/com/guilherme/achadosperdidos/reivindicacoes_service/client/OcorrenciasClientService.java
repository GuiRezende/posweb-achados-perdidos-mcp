package com.guilherme.achadosperdidos.reivindicacoes_service.client;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
public class OcorrenciasClientService {

    private final RestClient ocorrenciasRestClient;

    public void validarOcorrenciaExiste(Long ocorrenciaId) {
        try {
            ocorrenciasRestClient.get()
                    .uri("/ocorrencias/{id}", ocorrenciaId)
                    .retrieve()
                    .toBodilessEntity();
        } catch (HttpClientErrorException.NotFound e) {
            throw new IllegalArgumentException("Ocorrência não encontrada com ID: " + ocorrenciaId);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao comunicar com o serviço de Ocorrências: " + e.getMessage());
        }
    }
}