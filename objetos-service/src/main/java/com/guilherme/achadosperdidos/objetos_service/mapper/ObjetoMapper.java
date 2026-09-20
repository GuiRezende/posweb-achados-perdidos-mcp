package com.guilherme.achadosperdidos.objetos_service.mapper;

import com.guilherme.achadosperdidos.objetos_service.dto.ObjetoDTO;
import com.guilherme.achadosperdidos.objetos_service.dto.ObjetoResponseDTO;
import com.guilherme.achadosperdidos.objetos_service.entity.ObjetoEntity;
import org.springframework.stereotype.Component;

@Component
public class ObjetoMapper {

    public ObjetoDTO toDTO(ObjetoEntity objetoEntity){
        return ObjetoDTO.builder()
                .id(objetoEntity.getId())
                .nome(objetoEntity.getNome())
                .categoria(objetoEntity.getCategoria())
                .cor(objetoEntity.getCor())
                .marca(objetoEntity.getMarca())
                .descricao(objetoEntity.getDescricao())
                .caracteristicas(objetoEntity.getCaracteristicas())
                .build();
    }

    public ObjetoResponseDTO toResponse(ObjetoEntity objetoEntity){
        return ObjetoResponseDTO.builder()
                .id(objetoEntity.getId())
                .nome(objetoEntity.getNome())
                .categoria(objetoEntity.getCategoria())
                .cor(objetoEntity.getCor())
                .marca(objetoEntity.getMarca())
                .descricao(objetoEntity.getDescricao())
                .caracteristicas(objetoEntity.getCaracteristicas())
                .build();
    }

    public ObjetoEntity toEntity(ObjetoDTO objetoDTO){
        return ObjetoEntity.builder()
                .nome(objetoDTO.getNome())
                .categoria(objetoDTO.getCategoria())
                .cor(objetoDTO.getCor())
                .marca(objetoDTO.getMarca())
                .descricao(objetoDTO.getDescricao())
                .caracteristicas(objetoDTO.getCaracteristicas())
                .build();
    }
}
