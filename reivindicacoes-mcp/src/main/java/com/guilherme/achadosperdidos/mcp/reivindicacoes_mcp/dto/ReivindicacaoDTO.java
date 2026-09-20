package com.guilherme.achadosperdidos.mcp.reivindicacoes_mcp.dto;

public record ReivindicacaoDTO(
        Long id,
        Long ocorrenciaId,
        String nomeSolicitante,
        String email,
        String comprovacao,
        String status,
        String dataSolicitacao,
        String dataDevolucao
) {}