package com.guilherme.achadosperdidos.ocorrencias_service.service;

import com.guilherme.achadosperdidos.ocorrencias_service.client.ObjetosClientService;
import com.guilherme.achadosperdidos.ocorrencias_service.dto.OcorrenciaDTO;
import com.guilherme.achadosperdidos.ocorrencias_service.enums.StatusOcorrenciaEnum;
import com.guilherme.achadosperdidos.ocorrencias_service.enums.TipoOcorrenciaEnum;
import com.guilherme.achadosperdidos.ocorrencias_service.mapper.OcorrenciaMapper;
import com.guilherme.achadosperdidos.ocorrencias_service.repository.OcorrenciaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OcorrenciaService {

    private final ObjetosClientService objetoService;
    private final OcorrenciaRepository repository;
    private final OcorrenciaMapper mapper;

    public boolean atualizarStatus(Long id, StatusOcorrenciaEnum status) {
        return repository.findById(id)
                .map(ocorrencia -> {
                    ocorrencia.setStatus(status);
                    repository.save(ocorrencia);
                    return true;
        }).orElse(false);
    }

    public List<OcorrenciaDTO> getListaOcorrencias() {
        return repository.findAll()
                .stream()
                .map(mapper::toDTO)
                .toList();
    }

    public List<OcorrenciaDTO> getListaOcorrenciasPorTipoEStatus(TipoOcorrenciaEnum tipo, StatusOcorrenciaEnum status) {
        return repository.findByTipoOcorrenciaAndStatus(tipo, status).stream()
                .map(mapper::toDTO)
                .toList();
    }

    public Optional<OcorrenciaDTO> getOcorrencia(Long id) {
        return repository.findById(id)
                .map(mapper::toDTO);
    }

    public void insert(OcorrenciaDTO ocorrencia) {
        var objeto = objetoService.buscarObjetoPorId(ocorrencia.getObjetoId());
        if (objeto == null) {
            throw new IllegalArgumentException("Objeto não encontrado com id: " + ocorrencia.getObjetoId());
        }
        repository.save(mapper.toEntity(ocorrencia));
    }
}
