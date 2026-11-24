package com.knowya.knowya.dto;

import java.time.LocalDateTime;

public class MensajeResponseDTO {

    private Integer idMensaje;
    private Integer idChat;
    private Integer idUsuario;
    private String nombreUsuario;
    private String contenido;
    private LocalDateTime fechaEnvio;

    public MensajeResponseDTO() {
    }

    public MensajeResponseDTO(Integer idMensaje, Integer idChat, Integer idUsuario, String nombreUsuario, String contenido, LocalDateTime fechaEnvio) {
        this.idMensaje = idMensaje;
        this.idChat = idChat;
        this.idUsuario = idUsuario;
        this.nombreUsuario = nombreUsuario;
        this.contenido = contenido;
        this.fechaEnvio = fechaEnvio;
    }

    public Integer getIdMensaje() {
        return idMensaje;
    }

    public void setIdMensaje(Integer idMensaje) {
        this.idMensaje = idMensaje;
    }

    public Integer getIdChat() {
        return idChat;
    }

    public void setIdChat(Integer idChat) {
        this.idChat = idChat;
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

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public LocalDateTime getFechaEnvio() {
        return fechaEnvio;
    }

    public void setFechaEnvio(LocalDateTime fechaEnvio) {
        this.fechaEnvio = fechaEnvio;
    }
}
