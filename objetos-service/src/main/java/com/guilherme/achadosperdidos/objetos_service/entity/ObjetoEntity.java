package com.guilherme.achadosperdidos.objetos_service.entity;

import com.guilherme.achadosperdidos.objetos_service.enums.CategoriaObjetoEnum;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "objetos")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ObjetoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private CategoriaObjetoEnum categoria;

    @Column(nullable = false, length = 50)
    private String cor;

    @Column(length = 100)
    private String marca;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String descricao;

    @Column(columnDefinition = "TEXT")
    private String caracteristicas;
}