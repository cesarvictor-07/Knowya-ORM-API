package com.knowya.knowya.dto;

import java.time.LocalDateTime;

public class InteraccionResponseDTO {

    private Integer idInteraccion;
    private Integer idUsuario;
    private String nombreUsuario;
    private LocalDateTime fechaInteraccion;
    private String estado;

    public InteraccionResponseDTO(Integer idInteraccion, Integer idUsuario, String nombreUsuario, LocalDateTime fechaInteraccion, String estado) {
        this.idInteraccion = idInteraccion;
        this.idUsuario = idUsuario;
        this.nombreUsuario = nombreUsuario;
        this.fechaInteraccion = fechaInteraccion;
        this.estado = estado;
    }

    public Integer getIdInteraccion() {
        return idInteraccion;
    }

    public void setIdInteraccion(Integer idInteraccion) {
        this.idInteraccion = idInteraccion;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public LocalDateTime getFechaInteraccion() {
        return fechaInteraccion;
    }

    public void setFechaInteraccion(LocalDateTime fechaInteraccion) {
        this.fechaInteraccion = fechaInteraccion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
