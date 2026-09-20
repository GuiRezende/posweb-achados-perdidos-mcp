package com.guilherme.achadosperdidos.reivindicacoes_service.entity;

import com.guilherme.achadosperdidos.reivindicacoes_service.enums.StatusReivindicacaoEnum;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "reivindicacoes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReivindicacaoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ocorrencia_id", nullable = false)
    private Long ocorrenciaId;

    @Column(name = "nome_solicitante", nullable = false)
    private String nomeSolicitante;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false, length = 1000)
    private String comprovacao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusReivindicacaoEnum status;

    @Column(name = "data_solicitacao", nullable = false, updatable = false)
    private LocalDateTime dataSolicitacao;

    @Column(name = "data_devolucao")
    private LocalDateTime dataDevolucao;
}