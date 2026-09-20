package com.guilherme.achadosperdidos.objetos_service.repository;

import com.guilherme.achadosperdidos.objetos_service.entity.ObjetoEntity;
import com.guilherme.achadosperdidos.objetos_service.enums.CategoriaObjetoEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ObjetoRepository extends JpaRepository<ObjetoEntity, Long> {
    List<ObjetoEntity> findAllByCategoria(CategoriaObjetoEnum categoria);
    Optional<ObjetoEntity> findById(Long id);
}
