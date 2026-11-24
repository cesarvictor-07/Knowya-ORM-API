package com.knowya.knowya.repository;

import com.knowya.knowya.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    boolean existsByEmail(String email);

    boolean existsByUserName(String userName);

    Optional<Usuario> findByEmail(String email);

    boolean existsByEmailAndIdUserNot(String email, Integer idUser);

    boolean existsByUserNameAndIdUserNot(String userName, Integer idUser);
}
