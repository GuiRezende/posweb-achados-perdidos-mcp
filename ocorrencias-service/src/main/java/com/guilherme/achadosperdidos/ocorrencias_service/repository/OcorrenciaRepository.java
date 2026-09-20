package com.guilherme.achadosperdidos.ocorrencias_service.repository;

import com.guilherme.achadosperdidos.ocorrencias_service.entity.OcorrenciaEntity;
import com.guilherme.achadosperdidos.ocorrencias_service.enums.StatusOcorrenciaEnum;
import com.guilherme.achadosperdidos.ocorrencias_service.enums.TipoOcorrenciaEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OcorrenciaRepository extends JpaRepository<OcorrenciaEntity, Long> {
    List<OcorrenciaEntity> findByTipoOcorrenciaAndStatus(TipoOcorrenciaEnum tipo, StatusOcorrenciaEnum status);
    Optional<OcorrenciaEntity> findByObjetoId(Long objetoId);
}
