package com.knowya.knowya.dto;

public class ConsultaSoporteRequestDTO {

    private String asunto;
    private String descripcion;

    public ConsultaSoporteRequestDTO(String asunto, String descripcion) {
        this.asunto = asunto;
        this.descripcion = descripcion;
    }

    public ConsultaSoporteRequestDTO() {
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
}
