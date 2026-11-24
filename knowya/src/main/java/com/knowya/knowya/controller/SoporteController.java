package com.knowya.knowya.controller;

import com.knowya.knowya.dto.ConsultaSoporteRequestDTO;
import com.knowya.knowya.dto.ConsultaSoporteResponseDTO;
import com.knowya.knowya.dto.SoporteResponseDTO;
import com.knowya.knowya.entity.ConsultaSoporte;
import com.knowya.knowya.entity.Soporte;
import com.knowya.knowya.entity.Usuario;
import com.knowya.knowya.repository.UsuarioRepository;
import com.knowya.knowya.service.ConsultaSoporteService;
import com.knowya.knowya.service.SoporteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/soporte")
public class SoporteController {

    private final SoporteService soporteService;
    private final ConsultaSoporteService consultaSoporteService;
    private final UsuarioRepository usuarioRepository;

    public SoporteController(SoporteService soporteService, ConsultaSoporteService consultaSoporteService, UsuarioRepository usuarioRepository) {
        this.soporteService = soporteService;
        this.consultaSoporteService = consultaSoporteService;
        this.usuarioRepository = usuarioRepository;
    }

    private ConsultaSoporteResponseDTO convertirConsultaDTO(
            ConsultaSoporte consulta) {

        return new ConsultaSoporteResponseDTO(
                consulta.getIdConsulta(),
                consulta.getAsunto(),
                consulta.getDescripcion(),
                consulta.getFechaConsulta(),
                consulta.getEstado());
    }

    private SoporteResponseDTO convertirSoporteDTO(Soporte soporte) {

        return new SoporteResponseDTO(
                soporte.getIdSupport(),
                soporte.getNombre(),
                soporte.getEmail(),
                soporte.getTelefono());
    }

    private Usuario obtenerUsuarioAutenticado(
            Authentication authentication
    ) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "El usuario no está autenticado"
            );
        }

        String email = authentication.getName();

        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "No se ha podido identificar al usuario"
                ));
    }

    @GetMapping("/contacto")
    public ResponseEntity<SoporteResponseDTO> obtenerContacto() {

        return soporteService.obtenerContactoPredeterminado()
                .map(this::convertirSoporteDTO)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity
                        .status(HttpStatus.SERVICE_UNAVAILABLE)
                        .build());
    }

    @PostMapping("/consultas")
    public ResponseEntity<ConsultaSoporteResponseDTO> crearConsulta(
            @RequestBody ConsultaSoporteRequestDTO request,
            Authentication authentication) {

        if (request == null) {
            return ResponseEntity.badRequest().build();
        }

        Usuario usuario = obtenerUsuarioAutenticado(authentication);

        try {
            ConsultaSoporte consulta = consultaSoporteService.crearConsulta(
                    usuario.getIdUser(),
                    request.getAsunto(),
                    request.getDescripcion());

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(convertirConsultaDTO(consulta));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();

        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .build();
        }
    }

    @GetMapping("/consultas")
    public ResponseEntity<List<ConsultaSoporteResponseDTO>> obtenerMisConsultas(
            Authentication authentication) {

        Usuario usuario = obtenerUsuarioAutenticado(authentication);

        List<ConsultaSoporteResponseDTO> consultas =
                consultaSoporteService
                        .obtenerConsultasPorUsuario(usuario.getIdUser())
                        .stream()
                        .map(this::convertirConsultaDTO)
                        .toList();

        return ResponseEntity.ok(consultas);
    }

    @GetMapping("/consultas/{idConsulta}")
    public ResponseEntity<ConsultaSoporteResponseDTO> obtenerConsulta(
            @PathVariable Integer idConsulta,
            Authentication authentication) {

        Usuario usuario = obtenerUsuarioAutenticado(authentication);

        try {
            ConsultaSoporte consulta =
                    consultaSoporteService.obtenerConsultaPorId(
                            idConsulta,
                            usuario.getIdUser());

            return ResponseEntity.ok(convertirConsultaDTO(consulta));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
