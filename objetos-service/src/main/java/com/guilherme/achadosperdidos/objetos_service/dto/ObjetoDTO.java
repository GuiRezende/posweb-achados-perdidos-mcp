package com.guilherme.achadosperdidos.objetos_service.dto;

import com.guilherme.achadosperdidos.objetos_service.enums.CategoriaObjetoEnum;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ObjetoDTO {
    private Long id;
    private String nome;
    private CategoriaObjetoEnum categoria;
    private String cor;
    private String marca;
    private String descricao;
    private String caracteristicas;
}
