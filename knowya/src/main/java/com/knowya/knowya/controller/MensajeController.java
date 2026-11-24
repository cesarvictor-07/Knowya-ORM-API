package com.knowya.knowya.controller;


import com.knowya.knowya.dto.MensajeRequestDTO;
import com.knowya.knowya.dto.MensajeResponseDTO;
import com.knowya.knowya.entity.Usuario;
import com.knowya.knowya.repository.UsuarioRepository;
import com.knowya.knowya.service.MensajeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/chats/{idChat}/mensajes")
public class MensajeController {

    private final MensajeService mensajeService;
    private final UsuarioRepository usuarioRepository;

    public MensajeController(MensajeService mensajeService, UsuarioRepository usuarioRepository) {
        this.mensajeService = mensajeService;
        this.usuarioRepository = usuarioRepository;
    }

    private Usuario obtenerUsuarioAutenticado(
            Authentication authentication
    ) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException(
                    "El usuario no está autenticado."
            );
        }

        String email = authentication.getName();

        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException(
                        "No se ha encontrado el usuario autenticado."
                ));
    }

    @GetMapping
    public ResponseEntity<?> obtenerMensajes(
            @PathVariable Integer idChat,
            Authentication authentication
    ) {
        try {
            Usuario usuario = obtenerUsuarioAutenticado(authentication);

            List<MensajeResponseDTO> mensajes =
                    mensajeService.obtenerMensajesDTO(
                            idChat,
                            usuario.getIdUser()
                    );

            return ResponseEntity.ok(mensajes);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("mensaje", e.getMessage()));

        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("mensaje", e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<?> enviarMensaje(
            @PathVariable Integer idChat,
            @RequestBody MensajeRequestDTO solicitud,
            Authentication authentication
    ) {
        try {
            Usuario usuario = obtenerUsuarioAutenticado(authentication);

            if (solicitud == null) {
                return ResponseEntity.badRequest()
                        .body(Map.of(
                                "mensaje",
                                "La solicitud del mensaje es obligatoria."
                        ));
            }

            MensajeResponseDTO mensaje =
                    mensajeService.enviarMensaje(
                            idChat,
                            usuario.getIdUser(),
                            solicitud.getContenido()
                    );

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(mensaje);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("mensaje", e.getMessage()));

        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("mensaje", e.getMessage()));
        }
    }
}
