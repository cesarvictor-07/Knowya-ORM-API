package com.knowya.knowya.dto;

public class UsuarioResponseDTO {

    private Integer idUser;
    private String userName;
    private String email;
    private String descripcion;

    public UsuarioResponseDTO() {
    }

    public UsuarioResponseDTO(Integer idUser, String userName, String email, String descripcion) {
        this.idUser = idUser;
        this.userName = userName;
        this.email = email;
        this.descripcion = descripcion;
    }

    public Integer getIdUser() {
        return idUser;
    }

    public void setIdUser(Integer idUser) {
        this.idUser = idUser;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
