
package com.knowya.knowya.service;

import com.knowya.knowya.entity.Chat;
import com.knowya.knowya.entity.Interaccion;
import com.knowya.knowya.entity.Usuario;
import com.knowya.knowya.repository.InteraccionRepository;
import com.knowya.knowya.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.knowya.knowya.dto.InteraccionResponseDTO;

import java.util.stream.Collectors;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class InteraccionService {

    private static final String ESTADO_ACTIVA = "ACTIVA";
    private static final String ESTADO_RETIRADA = "RETIRADA";

    private final InteraccionRepository interaccionRepository;
    private final UsuarioRepository usuarioRepository;
    private final ChatService chatService;

    public InteraccionService(
            InteraccionRepository interaccionRepository,
            UsuarioRepository usuarioRepository,
            ChatService chatService
    ) {
        this.interaccionRepository = interaccionRepository;
        this.usuarioRepository = usuarioRepository;
        this.chatService = chatService;
    }

    @Transactional
    public Interaccion mostrarInteres(Integer idOrigen, Integer idDestino) {

        if (idOrigen == null || idDestino == null) {
            throw new IllegalArgumentException(
                    "Los identificadores de los usuarios son obligatorios."
            );
        }

        if (idOrigen.equals(idDestino)) {
            throw new IllegalArgumentException(
                    "No puedes mostrar interés en tu propio usuario."
            );
        }

        Usuario origen = usuarioRepository.findById(idOrigen)
                .orElseThrow(() -> new IllegalArgumentException(
                        "El usuario de origen no existe."
                ));

        Usuario destino = usuarioRepository.findById(idDestino)
                .orElseThrow(() -> new IllegalArgumentException(
                        "El usuario destinatario no existe."
                ));

        Optional<Interaccion> interaccionExistente =
                interaccionRepository
                        .findByUsuarioOrigen_IdUserAndUsuarioDestino_IdUser(
                                idOrigen,
                                idDestino
                        );

        Interaccion interaccionGuardada;

        if (interaccionExistente.isPresent()) {

            Interaccion interaccion = interaccionExistente.get();

            if (ESTADO_ACTIVA.equals(interaccion.getEstado())) {
                throw new IllegalStateException(
                        "Ya has mostrado interés en este usuario anteriormente."
                );
            }

            if (!ESTADO_RETIRADA.equals(interaccion.getEstado())) {
                throw new IllegalStateException(
                        "La interacción tiene un estado no válido."
                );
            }

            interaccion.setEstado(ESTADO_ACTIVA);
            interaccion.setFechaInteraccion(LocalDateTime.now());

            interaccionGuardada = interaccionRepository.save(interaccion);

        } else {

            Interaccion nuevaInteraccion = new Interaccion();

            nuevaInteraccion.setUsuarioOrigen(origen);
            nuevaInteraccion.setUsuarioDestino(destino);
            nuevaInteraccion.setFechaInteraccion(LocalDateTime.now());
            nuevaInteraccion.setEstado(ESTADO_ACTIVA);

            interaccionGuardada =
                    interaccionRepository.save(nuevaInteraccion);
        }

        boolean existeCoincidencia =
                interaccionRepository
                        .existsByUsuarioOrigen_IdUserAndUsuarioDestino_IdUserAndEstado(
                                idDestino,
                                idOrigen,
                                ESTADO_ACTIVA
                        );

        if (existeCoincidencia) {
            crearORecuperarChat(origen, destino);
        }

        return interaccionGuardada;
    }

    private void crearORecuperarChat(Usuario usuario1, Usuario usuario2) {

        Optional<Chat> chatExistente =
                chatService.obtenerChatEntreUsuarios(
                        usuario1.getIdUser(),
                        usuario2.getIdUser()
                );

        if (chatExistente.isPresent()) {

            Chat chat = chatExistente.get();

            chat.setActivo(true);
            chatService.guardar(chat);

        } else {

            Chat nuevoChat = new Chat();

            nuevoChat.setUsuario1(usuario1);
            nuevoChat.setUsuario2(usuario2);
            nuevoChat.setActivo(true);

            chatService.guardar(nuevoChat);
        }
    }

    @Transactional
    public Interaccion retirarInteres(Integer idOrigen, Integer idDestino) {

        if (idOrigen == null || idDestino == null) {
            throw new IllegalArgumentException(
                    "Los identificadores de los usuarios son obligatorios."
            );
        }

        Interaccion interaccion = interaccionRepository
                .findByUsuarioOrigen_IdUserAndUsuarioDestino_IdUser(
                        idOrigen,
                        idDestino
                )
                .orElseThrow(() -> new IllegalArgumentException(
                        "No existe una interacción entre estos usuarios."
                ));

        if (!ESTADO_ACTIVA.equals(interaccion.getEstado())) {
            throw new IllegalStateException(
                    "Esta interacción no se encuentra activa."
            );
        }

        interaccion.setEstado(ESTADO_RETIRADA);

        Interaccion interaccionActualizada =
                interaccionRepository.save(interaccion);

        Optional<Chat> chatExistente =
                chatService.obtenerChatEntreUsuarios(
                        idOrigen,
                        idDestino
                );

        if (chatExistente.isPresent()) {

            Chat chat = chatExistente.get();
            chat.setActivo(false);

            chatService.guardar(chat);
        }

        return interaccionActualizada;
    }

    @Transactional(readOnly = true)
    public List<Interaccion> obtenerTodas() {
        return interaccionRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Interaccion> obtenerPorId(Integer id) {
        return interaccionRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Interaccion> obtenerEnviadasPorUsuario(Integer idUser) {
        return interaccionRepository.findByUsuarioOrigen_IdUser(idUser);
    }

    @Transactional(readOnly = true)
    public List<Interaccion> obtenerRecibidasPorUsuario(Integer idUser) {
        return interaccionRepository.findByUsuarioDestino_IdUser(idUser);
    }

    @Transactional(readOnly = true)
    public boolean existeInteraccion(Integer idOrigen, Integer idDestino) {
        return interaccionRepository
                .existsByUsuarioOrigen_IdUserAndUsuarioDestino_IdUser(
                        idOrigen,
                        idDestino
                );
    }

    private InteraccionResponseDTO convertirDTO(
            Interaccion interaccion,
            Usuario otroUsuario) {

        return new InteraccionResponseDTO(
                interaccion.getIdInteraccion(),
                otroUsuario.getIdUser(),
                otroUsuario.getUserName(),
                interaccion.getFechaInteraccion(),
                interaccion.getEstado()
        );
    }

    @Transactional(readOnly = true)
    public List<InteraccionResponseDTO> obtenerEnviadasDTO(Integer idUser) {

        return interaccionRepository.findByUsuarioOrigen_IdUser(idUser)
                .stream()
                .map(interaccion -> convertirDTO(
                        interaccion,
                        interaccion.getUsuarioDestino()
                ))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<InteraccionResponseDTO> obtenerRecibidasDTO(Integer idUser) {

        return interaccionRepository.findByUsuarioDestino_IdUser(idUser)
                .stream()
                .map(interaccion -> convertirDTO(
                        interaccion,
                        interaccion.getUsuarioOrigen()
                ))
                .collect(Collectors.toList());
    }
}
