
package com.knowya.knowya.service;

import com.knowya.knowya.dto.ChatResponseDTO;
import com.knowya.knowya.entity.Chat;
import com.knowya.knowya.repository.ChatRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ChatService {

    private final ChatRepository chatRepository;

    public ChatService(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    public List<Chat> obtenerTodos() {
        return chatRepository.findAll();
    }

    public Optional<Chat> obtenerPorId(Integer id) {
        return chatRepository.findById(id);
    }

    public Chat guardar(Chat chat) {
        return chatRepository.save(chat);
    }

    public boolean eliminar(Integer id) {
        if (!chatRepository.existsById(id)) {
            return false;
        }

        chatRepository.deleteById(id);
        return true;
    }

    public List<Chat> obtenerChatsPorUsuario(Integer idUsuario) {
        return chatRepository
                .findByUsuario1_IdUserOrUsuario2_IdUser(
                        idUsuario, idUsuario
                );
    }

    public Optional<Chat> obtenerChatEntreUsuarios(
            Integer idUsuario1,
            Integer idUsuario2
    ) {
        return chatRepository
                .findByUsuario1_IdUserAndUsuario2_IdUserOrUsuario1_IdUserAndUsuario2_IdUser(
                        idUsuario1, idUsuario2,
                        idUsuario2, idUsuario1
                );
    }

    public boolean existeChatEntreUsuarios(
            Integer idUsuario1,
            Integer idUsuario2
    ) {
        return chatRepository
                .existsByUsuario1_IdUserAndUsuario2_IdUserOrUsuario1_IdUserAndUsuario2_IdUser(
                        idUsuario1, idUsuario2,
                        idUsuario2, idUsuario1
                );
    }

    @Transactional(readOnly = true)
    public List<ChatResponseDTO> obtenerChatsDTO(Integer idUsuario) {

        List<Chat> chats = obtenerChatsPorUsuario(idUsuario);

        return chats.stream()
                .map(chat -> convertirDTO(chat, idUsuario))
                .toList();
    }

    @Transactional(readOnly = true)
    public ChatResponseDTO obtenerChatDTO(
            Integer idChat,
            Integer idUsuario
    ) {
        if (idChat == null || idUsuario == null) {
            throw new IllegalArgumentException(
                    "Los identificadores del chat y del usuario son obligatorios."
            );
        }

        Chat chat = chatRepository.findById(idChat)
                .orElseThrow(() -> new IllegalArgumentException(
                        "El chat solicitado no existe."
                ));

        boolean esUsuario1 = chat.getUsuario1()
                .getIdUser().equals(idUsuario);

        boolean esUsuario2 = chat.getUsuario2()
                .getIdUser().equals(idUsuario);

        if (!esUsuario1 && !esUsuario2) {
            throw new IllegalStateException(
                    "No tienes permiso para acceder a este chat."
            );
        }

        return convertirDTO(chat, idUsuario);
    }

    private ChatResponseDTO convertirDTO(Chat chat, Integer idUsuario) {

        boolean esUsuario1 = chat.getUsuario1()
                .getIdUser().equals(idUsuario);

        var otroUsuario = esUsuario1
                ? chat.getUsuario2()
                : chat.getUsuario1();

        return new ChatResponseDTO(
                chat.getIdChat(),
                otroUsuario.getIdUser(),
                otroUsuario.getUserName(),
                chat.getActivo()
        );
    }
}
