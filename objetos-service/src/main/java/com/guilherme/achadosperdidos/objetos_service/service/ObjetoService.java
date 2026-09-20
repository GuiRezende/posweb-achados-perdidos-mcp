package com.guilherme.achadosperdidos.objetos_service.service;

import com.guilherme.achadosperdidos.objetos_service.dto.ObjetoResponseDTO;
import com.guilherme.achadosperdidos.objetos_service.entity.ObjetoEntity;
import com.guilherme.achadosperdidos.objetos_service.mapper.ObjetoMapper;
import com.guilherme.achadosperdidos.objetos_service.dto.ObjetoDTO;
import com.guilherme.achadosperdidos.objetos_service.enums.CategoriaObjetoEnum;
import com.guilherme.achadosperdidos.objetos_service.repository.ObjetoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ObjetoService {

    private final ObjetoRepository repository;
    private final ObjetoMapper mapper;

    public List<ObjetoDTO> getListaObjetos(){
        var objetosEntity =  repository.findAll();
        return objetosEntity.stream().map(mapper::toDTO).toList();
    }

    public Optional<ObjetoDTO> getObjeto(Long id) {
        return repository.findById(id).map(mapper::toDTO);
    }

    public List<ObjetoDTO> getObjetosPorCategoria(String categoria) {
        try {
            var categoriaObj = CategoriaObjetoEnum.valueOf(categoria.toUpperCase());
            var objetosEntity = repository.findAllByCategoria(categoriaObj);
            return objetosEntity.stream().map(mapper::toDTO).toList();
        } catch (IllegalArgumentException e){
            log.warn("[OBJETO-CONTROLLER] Nenhuma categoria informada nao mapeada: {}", categoria);
            return List.of();
        }
    }

    @Transactional
    public ObjetoResponseDTO insert(ObjetoDTO dto) {
        ObjetoEntity objeto = mapper.toEntity(dto);
        ObjetoEntity objetoSalvo = repository.save(objeto); // O banco gera e popula o ID aqui
        return mapper.toResponse(objetoSalvo); // Retorna o DTO com o ID preenchido
    }
}
