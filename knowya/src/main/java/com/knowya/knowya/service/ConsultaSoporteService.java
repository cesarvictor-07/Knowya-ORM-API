
package com.knowya.knowya.service;

import com.knowya.knowya.entity.ConsultaSoporte;
import com.knowya.knowya.entity.Soporte;
import com.knowya.knowya.entity.Usuario;
import com.knowya.knowya.repository.ConsultaSoporteRepository;
import com.knowya.knowya.repository.SoporteRepository;
import com.knowya.knowya.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ConsultaSoporteService {

    private final ConsultaSoporteRepository consultaSoporteRepository;
    private final SoporteRepository soporteRepository;
    private final UsuarioRepository usuarioRepository;

    public ConsultaSoporteService(
            ConsultaSoporteRepository consultaSoporteRepository,
            SoporteRepository soporteRepository,
            UsuarioRepository usuarioRepository) {

        this.consultaSoporteRepository = consultaSoporteRepository;
        this.soporteRepository = soporteRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public ConsultaSoporte crearConsulta(
            Integer idUsuario,
            String asunto,
            String descripcion) {

        if (idUsuario == null) {
            throw new IllegalArgumentException(
                    "El usuario es obligatorio");
        }

        if (asunto == null || asunto.isBlank()) {
            throw new IllegalArgumentException(
                    "El asunto de la consulta es obligatorio");
        }

        if (asunto.length() > 100) {
            throw new IllegalArgumentException(
                    "El asunto no puede superar los 100 caracteres");
        }

        if (descripcion == null || descripcion.isBlank()) {
            throw new IllegalArgumentException(
                    "La descripción de la consulta es obligatoria");
        }

        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new IllegalArgumentException(
                        "El usuario solicitado no existe"));

        Soporte soporte = soporteRepository.findFirstByOrderByIdSupportAsc()
                .orElseThrow(() -> new IllegalStateException(
                        "No hay ningún contacto de soporte configurado"));

        ConsultaSoporte consulta = new ConsultaSoporte();

        consulta.setUsuario(usuario);
        consulta.setSoporte(soporte);
        consulta.setAsunto(asunto.trim());
        consulta.setDescripcion(descripcion.trim());
        consulta.setFechaConsulta(LocalDateTime.now());
        consulta.setEstado("PENDIENTE");

        return consultaSoporteRepository.save(consulta);
    }

    @Transactional(readOnly = true)
    public List<ConsultaSoporte> obtenerConsultasPorUsuario(Integer idUsuario) {

        if (idUsuario == null) {
            throw new IllegalArgumentException(
                    "El identificador del usuario es obligatorio");
        }

        return consultaSoporteRepository
                .findByUsuario_IdUserOrderByFechaConsultaDesc(idUsuario);
    }

    @Transactional(readOnly = true)
    public ConsultaSoporte obtenerConsultaPorId(
            Integer idConsulta,
            Integer idUsuario) {

        if (idConsulta == null || idUsuario == null) {
            throw new IllegalArgumentException(
                    "Los identificadores de consulta y usuario son obligatorios");
        }

        ConsultaSoporte consulta = consultaSoporteRepository.findById(idConsulta)
                .orElseThrow(() -> new IllegalArgumentException(
                        "La consulta no existe"));

        if (!consulta.getUsuario().getIdUser().equals(idUsuario)) {
            throw new IllegalArgumentException(
                    "La consulta no existe");
        }

        return consulta;
    }
}
