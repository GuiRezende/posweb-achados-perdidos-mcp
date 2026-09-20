package com.guilherme.achadosperdidos.ocorrencias_service.mapper;

import com.guilherme.achadosperdidos.ocorrencias_service.dto.OcorrenciaDTO;
import com.guilherme.achadosperdidos.ocorrencias_service.entity.OcorrenciaEntity;
import org.springframework.stereotype.Component;

@Component
public class OcorrenciaMapper {

    public OcorrenciaDTO toDTO(OcorrenciaEntity entity) {
        return OcorrenciaDTO.builder()
                .id(entity.getId())
                .tipoOcorrencia(entity.getTipoOcorrencia())
                .status(entity.getStatus())
                .objetoId(entity.getObjetoId())
                .descricao(entity.getDescricao())
                .localizacao(entity.getLocalizacao())
                .dataOcorrencia(entity.getDataOcorrencia())
                .contatoNome(entity.getContatoNome())
                .contatoTelefone(entity.getContatoTelefone())
                .contatoEmail(entity.getContatoEmail())
                .observacoes(entity.getObservacoes())
                .criadoEm(entity.getCriadoEm())
                .atualizadoEm(entity.getAtualizadoEm())
                .build();
    }

    public OcorrenciaEntity toEntity(OcorrenciaDTO dto) {
        return OcorrenciaEntity.builder()
                .tipoOcorrencia(dto.getTipoOcorrencia())
                .status(dto.getStatus())
                .objetoId(dto.getObjetoId())
                .descricao(dto.getDescricao())
                .localizacao(dto.getLocalizacao())
                .dataOcorrencia(dto.getDataOcorrencia())
                .contatoNome(dto.getContatoNome())
                .contatoTelefone(dto.getContatoTelefone())
                .contatoEmail(dto.getContatoEmail())
                .observacoes(dto.getObservacoes())
                .build();
    }
}
