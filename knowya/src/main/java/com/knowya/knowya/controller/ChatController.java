
package com.knowya.knowya.controller;

import com.knowya.knowya.dto.ChatResponseDTO;
import com.knowya.knowya.service.ChatService;
import com.knowya.knowya.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/chats")
public class ChatController {

    private final ChatService chatService;
    private final UsuarioService usuarioService;

    public ChatController(
            ChatService chatService,
            UsuarioService usuarioService
    ) {
        this.chatService = chatService;
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public ResponseEntity<List<ChatResponseDTO>> obtenerChats(
            Authentication authentication
    ) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        var usuario = usuarioService.obtenerPorEmail(
                authentication.getName()
        );

        if (usuario.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        List<ChatResponseDTO> chats = chatService.obtenerChatsDTO(
                usuario.get().getIdUser()
        );

        return ResponseEntity.ok(chats);
    }

    @GetMapping("/{idChat}")
    public ResponseEntity<?> obtenerChat(
            @PathVariable Integer idChat,
            Authentication authentication
    ) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        var usuario = usuarioService.obtenerPorEmail(
                authentication.getName()
        );

        if (usuario.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            ChatResponseDTO chat = chatService.obtenerChatDTO(
                    idChat,
                    usuario.get().getIdUser()
            );

            return ResponseEntity.ok(chat);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("mensaje", e.getMessage()));

        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("mensaje", e.getMessage()));
        }
    }
}
