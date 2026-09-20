package com.guilherme.achadosperdidos.mcp.ocorrencias_mcp.tools;

import com.guilherme.achadosperdidos.mcp.ocorrencias_mcp.client.OcorrenciasRestClient;
import com.guilherme.achadosperdidos.mcp.ocorrencias_mcp.dto.OcorrenciaDTO;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OcorrenciasMcpTools {

    private final OcorrenciasRestClient ocorrenciasRestClient;

    public OcorrenciasMcpTools(OcorrenciasRestClient ocorrenciasRestClient) {
        this.ocorrenciasRestClient = ocorrenciasRestClient;
    }

    @Tool(description = "Lista todas as ocorrencias de objetos perdidos ou achados registradas no IFBA.")
    public List<OcorrenciaDTO> listarOcorrencias() {
        return ocorrenciasRestClient.listarTodas();
    }

    @Tool(description = "Busca os detalhes de uma ocorrencia especifica no IFBA pelo seu ID.")
    public OcorrenciaDTO buscarOcorrenciaPorId(@ToolParam(description = "ID da ocorrencia") Long id) {
        return ocorrenciasRestClient.buscarPorId(id);
    }

    @Tool(description = """
            Registra uma nova ocorrência de objeto perdido ou achado no campus do IFBA.
            Só use esta ferramenta depois que o usuário confirmar todos os dados obrigatórios:
            objetoId (essa informacao retorna apos o cadastro do objeto), para o tipoOcorrencia será PERDIDO(quando usado a palavra PERDIDO e seus sinonimos) ou ENCONTRADO(quando usado a palavra ENCONTRADO e seus sinonimos), 
            localizacao, dataOcorrencia (caso utilize palavras chaves como Hoje, Ontem, ante-ontem ou derivados, usar o LocalDate de acordo)
            (formato AAAA-MM-DD), descricao, contatoNome, contatoTelefone e contatoEmail.
            Nunca invente, complete ou use valores de exemplo para campos ausentes.
            Se qualquer dado estiver faltando, não chame a ferramenta: pergunte o dado ao usuário.
            """)
    public String registrarOcorrencia(
            @ToolParam(description = "ID do objeto associado no sistema") Long objetoId,
            @ToolParam(description = "Tipo da ocorrência: PERDIDO ou ENCONTRADO") String tipoOcorrencia,
            @ToolParam(description = "Local do campus onde ocorreu") String localizacao,
            @ToolParam(description = "Data da ocorrência no formato AAAA-MM-DD") String dataOcorrencia,
            @ToolParam(description = "Descricao detalhada do ocorrido") String descricao,
            @ToolParam(description = "Nome completo da pessoa que informou a ocorrência") String contatoNome,
            @ToolParam(description = "Telefone ou WhatsApp da pessoa que informou") String contatoTelefone,
            @ToolParam(description = "E-mail da pessoa que informou") String contatoEmail,
            @ToolParam(description = "Observações adicionais, se houver") String observacoes) {

        validarObrigatorio(objetoId, tipoOcorrencia, localizacao, dataOcorrencia, descricao,
                contatoNome, contatoTelefone, contatoEmail);
        OcorrenciaDTO dto = new OcorrenciaDTO(null, tipoOcorrencia, "ABERTA", objetoId, descricao,
                localizacao, dataOcorrencia, contatoNome, contatoTelefone, contatoEmail, observacoes);
        return ocorrenciasRestClient.registrar(dto);
    }

    private void validarObrigatorio(Object objetoId, String... valores) {
        if (objetoId == null || java.util.Arrays.stream(valores).anyMatch(valor -> valor == null || valor.isBlank())) {
            throw new IllegalArgumentException("Não é possível registrar a ocorrência sem todos os dados obrigatórios.");
        }
    }
}