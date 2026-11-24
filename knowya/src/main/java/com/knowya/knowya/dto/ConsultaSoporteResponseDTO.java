package com.knowya.knowya.dto;

import java.time.LocalDateTime;

public class ConsultaSoporteResponseDTO {

    private Integer idConsulta;
    private String asunto;
    private String descripcion;
    private LocalDateTime fechaConsulta;
    private String estado;

    public ConsultaSoporteResponseDTO(Integer idConsulta, String asunto, String descripcion, LocalDateTime fechaConsulta, String estado) {
        this.idConsulta = idConsulta;
        this.asunto = asunto;
        this.descripcion = descripcion;
        this.fechaConsulta = fechaConsulta;
        this.estado = estado;
    }

    public ConsultaSoporteResponseDTO() {
    }

    public Integer getIdConsulta() {
        return idConsulta;
    }

    public void setIdConsulta(Integer idConsulta) {
        this.idConsulta = idConsulta;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDateTime getFechaConsulta() {
        return fechaConsulta;
    }

    public void setFechaConsulta(LocalDateTime fechaConsulta) {
        this.fechaConsulta = fechaConsulta;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
