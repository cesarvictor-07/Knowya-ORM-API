package com.knowya.knowya.service;

import com.knowya.knowya.entity.Soporte;
import com.knowya.knowya.repository.SoporteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SoporteService {

    private final SoporteRepository soporteRepository;

    public SoporteService(SoporteRepository soporteRepository) {
        this.soporteRepository = soporteRepository;
    }

    public List<Soporte> obtenerTodos() {
        return soporteRepository.findAll();
    }

    public Optional<Soporte> obtenerPorId(Integer id) {
        return soporteRepository.findById(id);
    }

    public Optional<Soporte> obtenerContactoPredeterminado() {
        return soporteRepository.findFirstByOrderByIdSupportAsc();
    }
}
