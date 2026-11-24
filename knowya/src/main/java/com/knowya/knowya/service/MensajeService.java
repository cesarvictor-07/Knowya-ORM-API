package com.knowya.knowya.service;

import com.knowya.knowya.dto.MensajeResponseDTO;
import com.knowya.knowya.entity.Chat;
import com.knowya.knowya.entity.Mensaje;
import com.knowya.knowya.entity.Usuario;
import com.knowya.knowya.repository.ChatRepository;
import com.knowya.knowya.repository.MensajeRepository;
import com.knowya.knowya.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class MensajeService {

    private final MensajeRepository mensajeRepository;
    private final ChatRepository chatRepository;
    private final UsuarioRepository usuarioRepository;

    public MensajeService(MensajeRepository mensajeRepository, ChatRepository chatRepository, UsuarioRepository usuarioRepository) {
        this.mensajeRepository = mensajeRepository;
        this.chatRepository = chatRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public List<Mensaje> obtenerTodos() {
        return mensajeRepository.findAll();
    }

    public Optional<Mensaje> obtenerPorId(Integer id) {
        return mensajeRepository.findById(id);
    }

    private MensajeResponseDTO convertirDTO(Mensaje mensaje) {

        return new MensajeResponseDTO(
                mensaje.getIdMensaje(),
                mensaje.getChat().getIdChat(),
                mensaje.getUsuario().getIdUser(),
                mensaje.getUsuario().getUserName(),
                mensaje.getContenido(),
                mensaje.getFechaEnvio()
        );
    }

    private void comprobarParticipacion(Chat chat, Integer idUsuario) {

        boolean esUsuario1 = chat.getUsuario1()
                .getIdUser().equals(idUsuario);

        boolean esUsuario2 = chat.getUsuario2()
                .getIdUser().equals(idUsuario);

        if (!esUsuario1 && !esUsuario2) {
            throw new IllegalStateException(
                    "No tienes permiso para acceder a este chat."
            );
        }
    }

    @Transactional(readOnly = true)
    public List<MensajeResponseDTO> obtenerMensajesDTO(
            Integer idChat,
            Integer idUsuario
    ) {
        if (idChat == null || idUsuario == null) {
            throw new IllegalArgumentException(
                    "Los identificadores de chat y de usuario son obligatorios"
            );
        }
        Chat chat = chatRepository.findById(idChat).orElseThrow(() -> new IllegalArgumentException(
                "El chat solicitado no existe"
        ));

        comprobarParticipacion(chat, idUsuario);

        return mensajeRepository
                .findByChat_IdChatOrderByFechaEnvioAsc(idChat)
                .stream()
                .map(this::convertirDTO)
                .toList();
    }

    @Transactional
    public MensajeResponseDTO enviarMensaje(
            Integer idChat,
            Integer idUsuario,
            String contenido
    ) {
        if (idChat == null || idUsuario == null) {
            throw new IllegalArgumentException(
                    "Los identificadores del chat y del usuario son obligatorios."
            );
        }
        if (contenido == null || contenido.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "El contenido del mensaje no puede estar vacío."
            );
        }

        Chat chat = chatRepository.findById(idChat)
                .orElseThrow(() -> new IllegalArgumentException(
                        "El chat no existe."
                ));

        comprobarParticipacion(chat, idUsuario);

        if (!Boolean.TRUE.equals(chat.getActivo())) {
            throw new IllegalStateException(
                    "No se pueden enviar mensajes a un chat inactivo"
            );
        }

        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new IllegalArgumentException(
                        "El usuario remitente no existe."
                ));

        Mensaje mensaje = new Mensaje();
        mensaje.setChat(chat);
        mensaje.setUsuario(usuario);
        mensaje.setContenido(contenido);
        mensaje.setFechaEnvio(LocalDateTime.now());

        Mensaje mensajeGuardado = mensajeRepository.save(mensaje);

        return convertirDTO(mensajeGuardado);
    }
}
