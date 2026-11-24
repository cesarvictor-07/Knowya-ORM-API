
package com.knowya.knowya.security;

import com.knowya.knowya.entity.Usuario;
import com.knowya.knowya.repository.UsuarioRepository;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UsuarioRepository usuarioRepository;

    public JwtAuthenticationFilter(
            JwtService jwtService,
            UsuarioRepository usuarioRepository) {

        this.jwtService = jwtService;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String cabecera = request.getHeader("Authorization");

        if (cabecera == null || !cabecera.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = cabecera.substring(7);

        try {
            if (jwtService.validarToken(token)) {

                String email = jwtService.extraerEmail(token);

                Usuario usuario = usuarioRepository
                        .findByEmail(email)
                        .orElse(null);

                if (usuario != null &&
                        SecurityContextHolder.getContext()
                                .getAuthentication() == null) {

                    UsernamePasswordAuthenticationToken autenticacion =
                            new UsernamePasswordAuthenticationToken(
                                    usuario.getEmail(),
                                    null,
                                    List.of(new SimpleGrantedAuthority("ROLE_USER"))
                            );

                    SecurityContextHolder.getContext()
                            .setAuthentication(autenticacion);
                }
            }

        } catch (JwtException | IllegalArgumentException e) {
        }

        filterChain.doFilter(request, response);
    }
}
