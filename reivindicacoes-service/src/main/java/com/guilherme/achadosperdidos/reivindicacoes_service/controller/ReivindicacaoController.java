package com.guilherme.achadosperdidos.reivindicacoes_service.controller;

import com.guilherme.achadosperdidos.reivindicacoes_service.dto.ReivindicacaoRequestDTO;
import com.guilherme.achadosperdidos.reivindicacoes_service.dto.ReivindicacaoResponseDTO;
import com.guilherme.achadosperdidos.reivindicacoes_service.enums.StatusReivindicacaoEnum;
import com.guilherme.achadosperdidos.reivindicacoes_service.service.ReivindicacaoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping()
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Slf4j
public class ReivindicacaoController {

    private final ReivindicacaoService service;

    @PostMapping("/reivindicacoes")
    public ResponseEntity<ReivindicacaoResponseDTO> criar(@RequestBody ReivindicacaoRequestDTO dto) {
        log.info("[REIVINDICACAO-SERVICE] POST /reivindicacoes recebido. ocorrenciaId={}, nomeSolicitante={}, emailInformado={}, comprovacaoInformada={}, statusRecebido={}",
                dto != null ? dto.ocorrenciaId() : null,
                dto != null ? dto.nomeSolicitante() : null,
                dto != null && dto.email() != null && !dto.email().isBlank(),
                dto != null && dto.comprovacao() != null && !dto.comprovacao().isBlank(),
                dto != null ? dto.status() : null);
        ReivindicacaoResponseDTO resposta = service.criar(dto);
        log.info("[REIVINDICACAO-SERVICE] POST /reivindicacoes persistido. id={}, ocorrenciaId={}, status={}",
                resposta.id(), resposta.ocorrenciaId(), resposta.status());
        return ResponseEntity.status(HttpStatus.CREATED).body(resposta);
    }

    @GetMapping("/reivindicacoes")
    public ResponseEntity<List<ReivindicacaoResponseDTO>> listar(
            @RequestParam(required = false) StatusReivindicacaoEnum status) {
        return ResponseEntity.ok(service.listar(status));
    }

    @GetMapping("/reivindicacoes/{id}")
    public ResponseEntity<ReivindicacaoResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PatchMapping("/reivindicacoes/{id}/aprovar")
    public ResponseEntity<ReivindicacaoResponseDTO> aprovar(@PathVariable Long id) {
        return ResponseEntity.ok(service.aprovar(id));
    }

    @PatchMapping("/reivindicacoes/{id}/rejeitar")
    public ResponseEntity<ReivindicacaoResponseDTO> rejeitar(@PathVariable Long id) {
        return ResponseEntity.ok(service.rejeitar(id));
    }

    @PatchMapping("/reivindicacoes/{id}/devolver")
    public ResponseEntity<ReivindicacaoResponseDTO> devolver(@PathVariable Long id) {
        return ResponseEntity.ok(service.devolver(id));
    }
}