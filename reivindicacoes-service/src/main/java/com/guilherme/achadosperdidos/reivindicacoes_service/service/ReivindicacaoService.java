package com.guilherme.achadosperdidos.reivindicacoes_service.service;

import com.guilherme.achadosperdidos.reivindicacoes_service.client.OcorrenciasClientService;
import com.guilherme.achadosperdidos.reivindicacoes_service.dto.ReivindicacaoRequestDTO;
import com.guilherme.achadosperdidos.reivindicacoes_service.dto.ReivindicacaoResponseDTO;
import com.guilherme.achadosperdidos.reivindicacoes_service.entity.ReivindicacaoEntity;
import com.guilherme.achadosperdidos.reivindicacoes_service.enums.StatusReivindicacaoEnum;
import com.guilherme.achadosperdidos.reivindicacoes_service.mapper.ReivindicacaoMapper;
import com.guilherme.achadosperdidos.reivindicacoes_service.repository.ReivindicacaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReivindicacaoService {

    private final ReivindicacaoRepository repository;
    private final OcorrenciasClientService ocorrenciasClientService;
    private final ReivindicacaoMapper mapper;

    @Transactional
    public ReivindicacaoResponseDTO criar(ReivindicacaoRequestDTO dto) {
        ocorrenciasClientService.validarOcorrenciaExiste(dto.ocorrenciaId());

        ReivindicacaoEntity reivindicacao = mapper.toEntity(dto);
        if (reivindicacao.getDataSolicitacao() == null) {
            reivindicacao.setDataSolicitacao(LocalDateTime.now());
        }
        if (reivindicacao.getStatus() == null) {
            reivindicacao.setStatus(StatusReivindicacaoEnum.PENDENTE);
        }

        ReivindicacaoEntity salva = repository.save(reivindicacao);
        return mapper.toDTO(salva);
    }

    @Transactional(readOnly = true)
    public List<ReivindicacaoResponseDTO> listar(StatusReivindicacaoEnum status) {
        List<ReivindicacaoEntity> lista = (status != null)
                ? repository.findByStatus(status)
                : repository.findAll();

        return mapper.toDTOList(lista);
    }

    @Transactional(readOnly = true)
    public ReivindicacaoResponseDTO buscarPorId(Long id) {
        ReivindicacaoEntity entity = buscarEntidade(id);
        return mapper.toDTO(entity);
    }

    @Transactional
    public ReivindicacaoResponseDTO aprovar(Long id) {
        ReivindicacaoEntity r = buscarEntidade(id);
        if (r.getStatus() != StatusReivindicacaoEnum.PENDENTE) {
            throw new RuntimeException("Apenas reivindicações PENDENTES podem ser aprovadas.");
        }
        r.setStatus(StatusReivindicacaoEnum.APROVADA);
        return mapper.toDTO(repository.save(r));
    }

    @Transactional
    public ReivindicacaoResponseDTO rejeitar(Long id) {
        ReivindicacaoEntity r = buscarEntidade(id);
        if (r.getStatus() != StatusReivindicacaoEnum.PENDENTE) {
            throw new RuntimeException("Apenas reivindicações PENDENTES podem ser rejeitadas.");
        }
        r.setStatus(StatusReivindicacaoEnum.REJEITADA);
        return mapper.toDTO(repository.save(r));
    }

    @Transactional
    public ReivindicacaoResponseDTO devolver(Long id) {
        ReivindicacaoEntity r = buscarEntidade(id);
        if (r.getStatus() != StatusReivindicacaoEnum.APROVADA) {
            throw new RuntimeException("Apenas reivindicações APROVADAS podem ter a devolução confirmada.");
        }
        r.setStatus(StatusReivindicacaoEnum.DEVOLVIDA);
        r.setDataDevolucao(LocalDateTime.now());
        return mapper.toDTO(repository.save(r));
    }

    private ReivindicacaoEntity buscarEntidade(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Reivindicação não encontrada com ID: " + id));
    }
}
