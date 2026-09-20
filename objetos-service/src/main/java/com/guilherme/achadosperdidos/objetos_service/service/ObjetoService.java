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
import java.util.Locale;
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

    public List<ObjetoDTO> buscarPorAtributos(String nome, String categoria, String cor, String marca, String descricao) {
        return repository.findAll().stream()
                .filter(objeto -> correspondeAtributo(objeto.getNome(), nome)
                        && correspondeAtributo(objeto.getCor(), cor)
                        && correspondeCategoria(objeto.getCategoria(), categoria)
                        && correspondeAtributo(objeto.getMarca(), marca)
                        && correspondeAtributo(objeto.getDescricao(), descricao))
                .map(mapper::toDTO)
                .toList();
    }

    @Transactional
    public ObjetoResponseDTO insert(ObjetoDTO dto) {
        ObjetoEntity objeto = mapper.toEntity(dto);
        ObjetoEntity objetoSalvo = repository.save(objeto); // O banco gera e popula o ID aqui
        return mapper.toResponse(objetoSalvo); // Retorna o DTO com o ID preenchido
    }

    private boolean correspondeAtributo(String valorBanco, String valorFiltro) {
        if (valorFiltro == null || valorFiltro.isBlank()) {
            return true;
        }
        if (valorBanco == null) {
            return false;
        }
        return normalizar(valorBanco).contains(normalizar(valorFiltro));
    }

    private boolean correspondeCategoria(CategoriaObjetoEnum valorBanco, String valorFiltro) {
        if (valorFiltro == null || valorFiltro.isBlank()) {
            return true;
        }
        if (valorBanco == null) {
            return false;
        }
        return valorBanco.name().equalsIgnoreCase(valorFiltro.trim());
    }

    private String normalizar(String valor) {
        return valor == null ? "" : valor.trim().replaceAll("\\s+", " ").toLowerCase(Locale.ROOT);
    }
}
