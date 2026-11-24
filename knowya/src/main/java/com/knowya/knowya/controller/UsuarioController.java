package com.knowya.knowya.controller;

import com.knowya.knowya.dto.UsuarioRegistroDTO;
import com.knowya.knowya.dto.UsuarioResponseDTO;
import com.knowya.knowya.dto.UsuarioLoginDTO;
import com.knowya.knowya.dto.AuthResponseDTO;
import com.knowya.knowya.dto.UsuarioActualizacionDTO;
import com.knowya.knowya.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/registro")
    public ResponseEntity<?> registrar(
            @Valid @RequestBody UsuarioRegistroDTO datosUsuario) {

        try {
            UsuarioResponseDTO usuarioRegistrado =
                    usuarioService.registrar(datosUsuario);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(usuarioRegistrado);

        } catch (IllegalArgumentException e) {

            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(Map.of("mensaje", e.getMessage()));
        }
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(
            @Valid @RequestBody UsuarioLoginDTO datosLogin) {

        try {
            AuthResponseDTO respuesta =
                    usuarioService.login(datosLogin);

            return ResponseEntity.ok(respuesta);

        } catch (IllegalArgumentException e) {

            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("mensaje", e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> obtenerTodos() {

        List<UsuarioResponseDTO> usuarios =
                usuarioService.obtenerTodosDTO();

        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> obtenerPorId(
            @PathVariable Integer id) {

        return usuarioService.obtenerPorIdDTO(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(
            @PathVariable Integer id,
            @Valid @RequestBody UsuarioActualizacionDTO datosUsuario) {

        try {
            return usuarioService.actualizar(id, datosUsuario)
                    .map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());

        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(Map.of("mensaje", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        boolean eliminado = usuarioService.eliminar(id);

        if (!eliminado) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }

}