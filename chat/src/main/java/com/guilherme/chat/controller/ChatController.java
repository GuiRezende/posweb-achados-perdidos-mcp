package com.guilherme.chat.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ChatController {

    private final ChatClient chatClient;

    @PostMapping(value = "/chat", produces = MediaType.APPLICATION_JSON_VALUE)
    public ChatResponse conversar(@RequestBody ChatRequest request) {
        if (request == null || request.message() == null || request.message().isBlank()) {
            throw new IllegalArgumentException("A mensagem não pode estar vazia.");
        }

        StringBuilder contexto = new StringBuilder();
        contexto.append("""
                CONTEXTO DA CONVERSA ANTERIOR:
                Use este histórico para continuar o atendimento. Não reinicie o fluxo
                nem repita perguntas que já foram respondidas. Se já houver dados suficientes
                para uma etapa, avance para a próxima informação faltante.
                
                """);

        if (request.history() != null) {
            request.history().stream()
                    .filter(item -> item != null && item.role() != null && item.content() != null)
                    .limit(20)
                    .forEach(item -> contexto
                            .append(item.role().equals("assistant") ? "Assistente: " : "Usuário: ")
                            .append(item.content().trim())
                            .append("\n"));
        }

        contexto.append("\nMENSAGEM ATUAL DO USUÁRIO:\n")
                .append(request.message().trim());

        String reply = chatClient.prompt()
                .user(contexto.toString())
                .call()
                .content();

        return new ChatResponse(reply == null ? "Não consegui gerar uma resposta agora." : reply);
    }

    public record ChatRequest(String message, List<ChatMessage> history) {}
    public record ChatMessage(String role, String content) {}
    public record ChatResponse(String reply) {}
}
