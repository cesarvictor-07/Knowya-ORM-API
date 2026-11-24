package com.knowya.knowya.service;

import com.knowya.knowya.dto.UsuarioResponseDTO;
import com.knowya.knowya.dto.UsuarioRegistroDTO;
import com.knowya.knowya.dto.UsuarioLoginDTO;
import com.knowya.knowya.dto.AuthResponseDTO;
import com.knowya.knowya.dto.UsuarioActualizacionDTO;
import com.knowya.knowya.security.JwtService;
import com.knowya.knowya.entity.Usuario;
import com.knowya.knowya.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public List<Usuario> obtenerTodos() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> obtenerPorId(Integer id) {
        return usuarioRepository.findById(id);
    }

    public Optional<Usuario> obtenerPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    public Usuario guardar(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    public boolean eliminar(Integer id) {

        if (!usuarioRepository.existsById(id)) {
            return false;
        }

        usuarioRepository.deleteById(id);
        return true;
    }

    public boolean existePorId(Integer id) {
        return usuarioRepository.existsById(id);
    }

    public boolean existePorEmail(String email) {
        return usuarioRepository.existsByEmail(email);
    }

    public boolean existePorUserName(String userName) {
        return usuarioRepository.existsByUserName(userName);
    }

    public UsuarioResponseDTO registrar(UsuarioRegistroDTO datosUsuario) {

        if (usuarioRepository.existsByEmail(datosUsuario.getEmail())) {
            throw new IllegalArgumentException(
                    "El correo electrónico ya está registrado");
        }

        if (usuarioRepository.existsByUserName(datosUsuario.getUserName())) {
            throw new IllegalArgumentException(
                    "El nombre de usuario ya está registrado");
        }

        Usuario usuario = new Usuario();

        usuario.setUserName(datosUsuario.getUserName());
        usuario.setEmail(datosUsuario.getEmail());

        usuario.setPassword(
                passwordEncoder.encode(datosUsuario.getPassword()));

        Usuario usuarioGuardado = usuarioRepository.save(usuario);

        return convertirAResponseDTO(usuarioGuardado);
    }


    public AuthResponseDTO login(UsuarioLoginDTO datosLogin) {

        Usuario usuario = usuarioRepository
                .findByEmail(datosLogin.getEmail())
                .orElseThrow(() -> new IllegalArgumentException(
                        "El correo electrónico o la contraseña son incorrectos"
                ));

        if (!passwordEncoder.matches(
                datosLogin.getPassword(),
                usuario.getPassword())) {

            throw new IllegalArgumentException(
                    "El correo electrónico o la contraseña son incorrectos"
            );
        }

        String token = jwtService.generarToken(usuario.getEmail());

        UsuarioResponseDTO usuarioDTO =
                convertirAResponseDTO(usuario);

        return new AuthResponseDTO(token, usuarioDTO);
    }


    public List<UsuarioResponseDTO> obtenerTodosDTO() {
        return usuarioRepository.findAll()
                .stream()
                .map(this::convertirAResponseDTO)
                .toList();
    }

    public Optional<UsuarioResponseDTO> obtenerPorIdDTO(Integer id) {
        return usuarioRepository.findById(id)
                .map(this::convertirAResponseDTO);
    }

    public Optional<UsuarioResponseDTO> actualizar(
            Integer id,
            UsuarioActualizacionDTO datosUsuario) {

        Optional<Usuario> resultado = usuarioRepository.findById(id);

        if (resultado.isEmpty()) {
            return Optional.empty();
        }

        if (usuarioRepository.existsByEmailAndIdUserNot(
                datosUsuario.getEmail(), id)) {
            throw new IllegalArgumentException(
                    "El correo electrónico ya está registrado");
        }

        if (usuarioRepository.existsByUserNameAndIdUserNot(
                datosUsuario.getUserName(), id)) {
            throw new IllegalArgumentException(
                    "El nombre de usuario ya está registrado");
        }

        Usuario usuarioExistente = resultado.get();

        usuarioExistente.setUserName(datosUsuario.getUserName());
        usuarioExistente.setEmail(datosUsuario.getEmail());
        usuarioExistente.setDescripcion(datosUsuario.getDescripcion());

        Usuario usuarioActualizado =
                usuarioRepository.save(usuarioExistente);

        return Optional.of(convertirAResponseDTO(usuarioActualizado));
    }


    private UsuarioResponseDTO convertirAResponseDTO(Usuario usuario) {
        return new UsuarioResponseDTO(
                usuario.getIdUser(),
                usuario.getUserName(),
                usuario.getEmail(),
                usuario.getDescripcion()
        );
    }

}
