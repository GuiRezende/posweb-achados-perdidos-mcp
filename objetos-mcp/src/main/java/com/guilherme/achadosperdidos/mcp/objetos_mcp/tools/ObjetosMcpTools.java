package com.guilherme.achadosperdidos.mcp.objetos_mcp.tools;

import com.guilherme.achadosperdidos.mcp.objetos_mcp.client.ObjetosRestClient;
import com.guilherme.achadosperdidos.mcp.objetos_mcp.dto.ObjetoDTO;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ObjetosMcpTools {

    private final ObjetosRestClient objetosClient;

    public ObjetosMcpTools(ObjetosRestClient objetosClient) {
        this.objetosClient = objetosClient;
    }

    @Tool(description = "Lista todos os objetos cadastrados no sistema de achados e perdidos do IFBA.")
    public List<ObjetoDTO> listarTodosObjetos() {
        return objetosClient.listarObjetos();
    }

    @Tool(description = "Busca objetos por nome, cor ou categoria para verificar se um item semelhante ja existe e evitar duplicidade.")
    public List<ObjetoDTO> buscarObjetosPorFiltro(
            @ToolParam(description = "Nome do objeto (ex: garrafa, mochila, fone)") String nome,
            @ToolParam(description = "Cor do objeto (ex: preta, azul)") String cor) {

        List<ObjetoDTO> todos = objetosClient.listarObjetos();

        return todos.stream()
                .filter(Objto -> (nome == null || Objto.getNome().toLowerCase().contains(nome.toLowerCase())) &&
                        (cor == null || Objto.getCor().toLowerCase().contains(cor.toLowerCase())))
                .toList();
    }

    @Tool(description = """
            Cadastra um novo objeto no catálogo do sistema.
            Só use depois de coletar nome, categoria, cor e descrição.
            Marca e características podem ficar vazias quando o usuário não souber.
            Nunca invente valores ausentes e não use esta ferramenta para reivindicação.
            """)
    public ObjetoDTO cadastrarObjeto(
            @ToolParam(description = "Nome do objeto") String nome,
            @ToolParam(description = "Categoria do objeto (ELETRONICO, VESTUARIO, ACESSORIO, DOCUMENTO, OUTRO)") String categoria,
            @ToolParam(description = "Cor principal") String cor,
            @ToolParam(description = "Marca do objeto (se aplicavel)") String marca,
            @ToolParam(description = "Descricao detalhada") String descricao,
            @ToolParam(description = "Caracteristicas marcantes ou adicionais") String caracteristicas) {

        if (nome == null || nome.isBlank() || categoria == null || categoria.isBlank()
                || cor == null || cor.isBlank() || descricao == null || descricao.isBlank()) {
            throw new IllegalArgumentException("Nome, categoria, cor e descrição são obrigatórios para cadastrar o objeto.");
        }

        ObjetoDTO dto = ObjetoDTO.builder()
                .nome(nome)
                .categoria(categoria)
                .cor(cor)
                .marca(marca)
                .descricao(descricao)
                .caracteristicas(caracteristicas)
                .build();

        return objetosClient.cadastrarObjeto(dto);
    }
}