package com.knowya.knowya.dto;

public class SoporteResponseDTO {

    private Integer idSupport;
    private String nombre;
    private String email;
    private String telefono;

    public SoporteResponseDTO(Integer idSupport, String nombre, String email, String telefono) {
        this.idSupport = idSupport;
        this.nombre = nombre;
        this.email = email;
        this.telefono = telefono;
    }

    public SoporteResponseDTO() {
    }

    public Integer getIdSupport() {
        return idSupport;
    }

    public void setIdSupport(Integer idSupport) {
        this.idSupport = idSupport;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}
