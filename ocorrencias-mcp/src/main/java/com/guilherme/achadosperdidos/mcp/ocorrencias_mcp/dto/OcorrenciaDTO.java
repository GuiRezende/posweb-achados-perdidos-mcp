package com.guilherme.achadosperdidos.mcp.ocorrencias_mcp.dto;

public record OcorrenciaDTO(
        Long id,
        String tipoOcorrencia,
        String status,
        Long objetoId,
        String descricao,
        String localizacao,
        String dataOcorrencia,
        String contatoNome,
        String contatoTelefone,
        String contatoEmail,
        String observacoes
) {}