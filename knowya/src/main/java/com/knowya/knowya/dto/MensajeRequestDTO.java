package com.knowya.knowya.dto;

public class MensajeRequestDTO {

    private String contenido;

    public MensajeRequestDTO() {
    }

    public MensajeRequestDTO(String contenido) {
        this.contenido = contenido;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }
}
