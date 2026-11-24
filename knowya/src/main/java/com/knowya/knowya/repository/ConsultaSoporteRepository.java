package com.knowya.knowya.repository;

import com.knowya.knowya.entity.ConsultaSoporte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConsultaSoporteRepository extends JpaRepository<ConsultaSoporte, Integer> {

    List<ConsultaSoporte> findByUsuario_IdUserOrderByFechaConsultaDesc(Integer idUser);

}
