package com.guilherme.achadosperdidos.reivindicacoes_service.repository;

import com.guilherme.achadosperdidos.reivindicacoes_service.entity.ReivindicacaoEntity;
import com.guilherme.achadosperdidos.reivindicacoes_service.enums.StatusReivindicacaoEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReivindicacaoRepository extends JpaRepository<ReivindicacaoEntity, Long> {
    List<ReivindicacaoEntity> findByStatus(StatusReivindicacaoEnum status);

}
