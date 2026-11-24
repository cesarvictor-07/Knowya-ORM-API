package com.knowya.knowya.repository;

import com.knowya.knowya.entity.Mensaje;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MensajeRepository extends JpaRepository<Mensaje, Integer> {

    List<Mensaje> findByChat_IdChatOrderByFechaEnvioAsc(Integer idChat);
}
