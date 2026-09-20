package com.guilherme.achadosperdidos.reivindicacoes_service.mapper;

import com.guilherme.achadosperdidos.reivindicacoes_service.dto.ReivindicacaoRequestDTO;
import com.guilherme.achadosperdidos.reivindicacoes_service.dto.ReivindicacaoResponseDTO;
import com.guilherme.achadosperdidos.reivindicacoes_service.entity.ReivindicacaoEntity;
import com.guilherme.achadosperdidos.reivindicacoes_service.enums.StatusReivindicacaoEnum;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ReivindicacaoMapper {

    public ReivindicacaoEntity toEntity(ReivindicacaoRequestDTO dto) {
        if (dto == null) return null;

        return ReivindicacaoEntity.builder()
                .ocorrenciaId(dto.ocorrenciaId())
                .nomeSolicitante(dto.nomeSolicitante())
                .email(dto.email())
                .comprovacao(dto.comprovacao())
                .status(StatusReivindicacaoEnum.PENDENTE)
                .build();
    }

    public ReivindicacaoResponseDTO toDTO(ReivindicacaoEntity entity) {
        if (entity == null) return null;

        return new ReivindicacaoResponseDTO(
                entity.getId(),
                entity.getOcorrenciaId(),
                entity.getNomeSolicitante(),
                entity.getEmail(),
                entity.getComprovacao(),
                entity.getStatus(),
                entity.getDataSolicitacao(),
                entity.getDataDevolucao()
        );
    }

    public List<ReivindicacaoResponseDTO> toDTOList(List<ReivindicacaoEntity> entities) {
        if (entities == null) return List.of();
        return entities.stream()
                .map(this::toDTO)
                .toList();
    }
}