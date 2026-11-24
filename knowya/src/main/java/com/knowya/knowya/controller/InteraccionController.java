
package com.knowya.knowya.controller;

import com.knowya.knowya.dto.InteraccionResponseDTO;
import com.knowya.knowya.entity.Interaccion;
import com.knowya.knowya.entity.Usuario;
import com.knowya.knowya.repository.UsuarioRepository;
import com.knowya.knowya.service.InteraccionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/interacciones")
public class InteraccionController {

    private final InteraccionService interaccionService;
    private final UsuarioRepository usuarioRepository;

    public InteraccionController(
            InteraccionService interaccionService,
            UsuarioRepository usuarioRepository) {

        this.interaccionService = interaccionService;
        this.usuarioRepository = usuarioRepository;
    }

    private Usuario obtenerUsuarioAutenticado(
            Authentication authentication) {

        if (authentication == null ||
                !authentication.isAuthenticated()) {

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

    @PostMapping("/{idDestino}")
    public ResponseEntity<?> mostrarInteres(
            @PathVariable Integer idDestino,
            Authentication authentication) {

        try {

            Usuario usuarioAutenticado =
                    obtenerUsuarioAutenticado(authentication);

            Interaccion interaccion =
                    interaccionService.mostrarInteres(
                            usuarioAutenticado.getIdUser(),
                            idDestino
                    );

            Usuario usuarioDestino =
                    interaccion.getUsuarioDestino();

            InteraccionResponseDTO respuesta =
                    new InteraccionResponseDTO(
                            interaccion.getIdInteraccion(),
                            usuarioDestino.getIdUser(),
                            usuarioDestino.getUserName(),
                            interaccion.getFechaInteraccion(),
                            interaccion.getEstado()
                    );

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(respuesta);

        } catch (IllegalArgumentException e) {

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("mensaje", e.getMessage()));

        } catch (IllegalStateException e) {

            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(Map.of("mensaje", e.getMessage()));
        }
    }

    @DeleteMapping("/{idDestino}")
    public ResponseEntity<?> retirarInteres(
            @PathVariable Integer idDestino,
            Authentication authentication) {

        try {

            Usuario usuarioAutenticado =
                    obtenerUsuarioAutenticado(authentication);

            interaccionService.retirarInteres(
                    usuarioAutenticado.getIdUser(),
                    idDestino
            );

            return ResponseEntity.noContent().build();

        } catch (IllegalArgumentException e) {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of("mensaje", e.getMessage()));

        } catch (IllegalStateException e) {

            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(Map.of("mensaje", e.getMessage()));
        }
    }

    @GetMapping("/enviadas")
    public ResponseEntity<List<InteraccionResponseDTO>>
    obtenerEnviadas(Authentication authentication) {

        Usuario usuarioAutenticado =
                obtenerUsuarioAutenticado(authentication);

        List<InteraccionResponseDTO> interacciones =
                interaccionService.obtenerEnviadasDTO(
                        usuarioAutenticado.getIdUser()
                );

        return ResponseEntity.ok(interacciones);
    }

    @GetMapping("/recibidas")
    public ResponseEntity<List<InteraccionResponseDTO>>
    obtenerRecibidas(Authentication authentication) {

        Usuario usuarioAutenticado =
                obtenerUsuarioAutenticado(authentication);

        List<InteraccionResponseDTO> interacciones =
                interaccionService.obtenerRecibidasDTO(
                        usuarioAutenticado.getIdUser()
                );

        return ResponseEntity.ok(interacciones);
    }
}
