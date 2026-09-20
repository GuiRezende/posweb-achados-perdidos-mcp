package com.guilherme.achadosperdidos.ocorrencias_service.dto;

import com.guilherme.achadosperdidos.ocorrencias_service.enums.StatusOcorrenciaEnum;
import com.guilherme.achadosperdidos.ocorrencias_service.enums.TipoOcorrenciaEnum;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OcorrenciaDTO {
    private Long id;
    private TipoOcorrenciaEnum tipoOcorrencia;
    private StatusOcorrenciaEnum status;
    private Long objetoId;
    private String descricao;
    private String localizacao;
    private LocalDate dataOcorrencia;
    private String contatoNome;
    private String contatoTelefone;
    private String contatoEmail;
    private String observacoes;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;
}
