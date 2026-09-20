package com.guilherme.achadosperdidos.reivindicacoes_service.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
@Slf4j
public class OcorrenciasClientService {

    private final RestClient ocorrenciasRestClient;

    public void validarOcorrenciaExiste(Long ocorrenciaId) {
        log.info("[REIVINDICACAO-SERVICE] Consultando ocorrência antes de persistir. GET /ocorrencias/{}", ocorrenciaId);
        try {
            ocorrenciasRestClient.get()
                    .uri("/ocorrencias/{id}", ocorrenciaId)
                    .retrieve()
                    .toBodilessEntity();
            log.info("[REIVINDICACAO-SERVICE] Ocorrência encontrada. ocorrenciaId={}", ocorrenciaId);
        } catch (HttpClientErrorException.NotFound e) {
            log.warn("[REIVINDICACAO-SERVICE] Ocorrência não encontrada. ocorrenciaId={}", ocorrenciaId);
            throw new IllegalArgumentException("Ocorrência não encontrada com ID: " + ocorrenciaId);
        } catch (Exception e) {
            log.error("[REIVINDICACAO-SERVICE] Falha ao consultar ocorrência. ocorrenciaId={}, tipo={}, mensagem={}",
                    ocorrenciaId, e.getClass().getSimpleName(), e.getMessage(), e);
            throw new RuntimeException("Erro ao comunicar com o serviço de Ocorrências: " + e.getMessage());
        }
    }
}