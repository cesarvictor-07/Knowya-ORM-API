package com.knowya.knowya.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user")
    private Integer idUser;

    @Column(name = "username", length = 60, nullable = false, unique = true)
    private String userName;

    @Column(name = "email", length = 80, nullable = false, unique = true)
    private String email;

    @Column(name = "password", length = 255, nullable = false)
    private String password;

    @Column(name = "descripcion", length = 255)
    private String descripcion;

    @OneToMany(mappedBy = "usuarioOrigen", cascade = CascadeType.REMOVE)
    private List<Interaccion> interaccionesEnviadas = new ArrayList<>();

    @OneToMany(mappedBy = "usuarioDestino", cascade = CascadeType.REMOVE)
    private List<Interaccion> interaccionesRecibidas = new ArrayList<>();

    @OneToMany(mappedBy = "usuario1", cascade = CascadeType.REMOVE)
    private List<Chat> chatsComoUsuario1 = new ArrayList<>();

    @OneToMany(mappedBy = "usuario2", cascade = CascadeType.REMOVE)
    private List<Chat> chatsComoUsuario2 = new ArrayList<>();

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.REMOVE)
    private List<Mensaje> mensajesEnviados = new ArrayList<>();

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.REMOVE)
    private List<ConsultaSoporte> consultasSoporte = new ArrayList<>();

    public Usuario() {
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
