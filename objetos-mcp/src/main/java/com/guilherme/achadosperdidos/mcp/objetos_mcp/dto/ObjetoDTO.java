package com.guilherme.achadosperdidos.mcp.objetos_mcp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ObjetoDTO {
    private Long id;
    private String nome;
    private String categoria;
    private String cor;
    private String marca;
    private String descricao;
    private String caracteristicas;
}