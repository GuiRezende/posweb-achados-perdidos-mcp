package com.guilherme.achadosperdidos.ocorrencias_service.entity;

import com.guilherme.achadosperdidos.ocorrencias_service.enums.TipoOcorrenciaEnum;
import com.guilherme.achadosperdidos.ocorrencias_service.enums.StatusOcorrenciaEnum;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "ocorrencias")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OcorrenciaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private TipoOcorrenciaEnum tipoOcorrencia;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private StatusOcorrenciaEnum status;

    @Column(name = "objeto_id", nullable = false)
    private Long objetoId;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String descricao;

    @Column(length = 120)
    private String localizacao;

    @Column(name = "data_ocorrencia", nullable = false)
    private LocalDate dataOcorrencia;

    @Column(length = 120)
    private String contatoNome;

    @Column(length = 30)
    private String contatoTelefone;

    @Column(length = 120)
    private String contatoEmail;

    @Column(columnDefinition = "TEXT")
    private String observacoes;

    @CreationTimestamp
    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @UpdateTimestamp
    @Column(name = "atualizado_em")
    private LocalDateTime atualizadoEm;
}