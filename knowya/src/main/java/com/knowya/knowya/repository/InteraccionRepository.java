package com.knowya.knowya.repository;

import com.knowya.knowya.entity.Interaccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InteraccionRepository extends JpaRepository<Interaccion, Integer> {
    List<Interaccion> findByUsuarioOrigen_IdUser(Integer idUser);

    List<Interaccion> findByUsuarioDestino_IdUser(Integer idUser);

    boolean existsByUsuarioOrigen_IdUserAndUsuarioDestino_IdUser(
            Integer idOrigen,
            Integer idDestino
    );

    boolean existsByUsuarioOrigen_IdUserAndUsuarioDestino_IdUserAndEstado(
            Integer idOrigen,
            Integer idDestino,
            String estado
    );

    Optional<Interaccion> findByUsuarioOrigen_IdUserAndUsuarioDestino_IdUser(
            Integer idOrigen,
            Integer idDestino
    );
}