package com.knowya.knowya.repository;

import com.knowya.knowya.entity.Soporte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SoporteRepository extends JpaRepository<Soporte, Integer> {

    Optional<Soporte> findFirstByOrderByIdSupportAsc();

}
