package com.guilherme.achadosperdidos.objetos_service.controller;

import com.guilherme.achadosperdidos.objetos_service.dto.ObjetoDTO;
import com.guilherme.achadosperdidos.objetos_service.dto.ObjetoResponseDTO;
import com.guilherme.achadosperdidos.objetos_service.service.ObjetoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping()
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ObjetoController {

    private final ObjetoService service;

    private static final String INFO_API_OBJETOS = """
            {
                "projeto": "API de Objetos",
                "descricao": "Responsável pelo cadastro e pela consulta das características dos objetos",
                "versao": "1.0.0",
                "desenvolvedor": "Guilherme Rezende Inacio"
            }
            """;

    @GetMapping(path = {"/", ""}, produces = MediaType.APPLICATION_JSON_VALUE)
    public String obterInformacoes() {
        return INFO_API_OBJETOS;
    }

    @GetMapping("/objetos")
    public ResponseEntity<List<ObjetoDTO>> listarObjetos(@RequestParam(required = false) String categoria) {
        List<ObjetoDTO> objetos;

        if (categoria != null && !categoria.isBlank()) {
            objetos = service.getObjetosPorCategoria(categoria);
        } else {
            objetos = service.getListaObjetos();
        }

        return ResponseEntity.ok(objetos);
    }

    @GetMapping("/objetos/{id}")
    public ResponseEntity<ObjetoDTO> buscarObjetoPorId(@PathVariable Long id) {
        return service.getObjeto(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/objetos")
    public ResponseEntity<ObjetoResponseDTO> registrarObjeto(@RequestBody ObjetoDTO objeto) {
        ObjetoResponseDTO objetoSalvo = service.insert(objeto);
        return ResponseEntity.status(HttpStatus.CREATED).body(objetoSalvo);
    }
}