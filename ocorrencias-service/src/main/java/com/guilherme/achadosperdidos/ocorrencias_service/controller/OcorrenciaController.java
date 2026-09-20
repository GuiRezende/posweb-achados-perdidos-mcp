package com.guilherme.achadosperdidos.ocorrencias_service.controller;

import com.guilherme.achadosperdidos.ocorrencias_service.dto.OcorrenciaDTO;
import com.guilherme.achadosperdidos.ocorrencias_service.enums.StatusOcorrenciaEnum;
import com.guilherme.achadosperdidos.ocorrencias_service.enums.TipoOcorrenciaEnum;
import com.guilherme.achadosperdidos.ocorrencias_service.service.OcorrenciaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping()
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Slf4j
public class OcorrenciaController {

    private final OcorrenciaService service;

    private static final String INFO_API_OCORRENCIAS = """
            {
                "projeto": "API de Ocorrencias",
                "descricao": "Responsável pelo registro dos objetos perdidos ou encontrados, com data, local e situação",
                "versao": "1.0.0",
                "desenvolvedor": "Guilherme Rezende Inacio"
            }
            """;

    @GetMapping(path = {"/", ""}, produces = MediaType.APPLICATION_JSON_VALUE)
    public String obterInformacoes() {
        return INFO_API_OCORRENCIAS;
    }

    @GetMapping("/ocorrencias")
    public ResponseEntity<List<OcorrenciaDTO>> listar(@RequestParam(required = false) TipoOcorrenciaEnum tipo,
                                                      @RequestParam(required = false) StatusOcorrenciaEnum status) {
        if (tipo == null && status == null)
            return ResponseEntity.ok(service.getListaOcorrencias());
        return ResponseEntity.ok(service.getListaOcorrenciasPorTipoEStatus(tipo, status));
    }

    @GetMapping("/ocorrencias/{id}")
    public ResponseEntity<OcorrenciaDTO> buscarOcorrenciaPorId(@PathVariable Long id) {
        return service.getOcorrencia(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/ocorrencias")
    public ResponseEntity<String> registrarOcorrencia(@RequestBody OcorrenciaDTO ocorrencia) {
        log.info("[OCORRENCIA-CONTROLLER] Registrando Ocorrencia: {}", ocorrencia);
        service.insert(ocorrencia);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("Ocorrencia Registrado Com Sucesso!");
    }

    @PatchMapping("/ocorrencias/{id}/status")
    public ResponseEntity<String> atualizarStatusOcorrencia(@PathVariable Long id, @RequestParam StatusOcorrenciaEnum status) {
        boolean atualizado = service.atualizarStatus(id, status);
        if (atualizado) {
            return ResponseEntity.ok("Status da Ocorrência atualizado com sucesso!");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    public record OcorrenciaDtoResponse (
            Long id,
            TipoOcorrenciaEnum tipoOcorrencia,
            StatusOcorrenciaEnum status,
            Long objetoId,
            String descricao,
            String localizacao,
            LocalDate dataOcorrencia,
            String contatoNome,
            String contatoTelefone,
            String contatoEmail,
            String observacoes,
            LocalDateTime criadoEm,
            LocalDateTime atualizadoEm
    ){}
}
