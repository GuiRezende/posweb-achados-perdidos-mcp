package com.guilherme.achadosperdidos.reivindicacoes_service.dto;

import com.guilherme.achadosperdidos.reivindicacoes_service.enums.StatusReivindicacaoEnum;
import java.time.LocalDateTime;

public record ReivindicacaoResponseDTO(
        Long id,
        Long ocorrenciaId,
        String nomeSolicitante,
        String email,
        String comprovacao,
        StatusReivindicacaoEnum status,
        LocalDateTime dataSolicitacao,
        LocalDateTime dataDevolucao
) {}