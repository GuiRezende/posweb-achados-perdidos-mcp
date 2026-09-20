package com.guilherme.achadosperdidos.mcp.reivindicacoes_mcp.dto;

public record ReivindicacaoDTO(
        Long id,
        Long objetoId,
        String solicitanteMatricula,
        String justificativa,
        String status, // PENDENTE, APROVADA, REJEITADA
        String dataHora
) {}