package com.knowya.knowya.repository;

import com.knowya.knowya.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Integer> {

    List<Chat> findByUsuario1_IdUserOrUsuario2_IdUser(
            Integer idUsuario1,
            Integer idUsuario2
    );

    Optional<Chat> findByUsuario1_IdUserAndUsuario2_IdUserOrUsuario1_IdUserAndUsuario2_IdUser(
            Integer idUsuario1,
            Integer idUsuario2,
            Integer idUsuario2Alternativo,
            Integer idUsuario1Alternativo
    );

    boolean existsByUsuario1_IdUserAndUsuario2_IdUserOrUsuario1_IdUserAndUsuario2_IdUser(
            Integer idUsuario1,
            Integer idUsuario2,
            Integer idUsuario2Alternativo,
            Integer idUsuario1Alternativo
    );
}
