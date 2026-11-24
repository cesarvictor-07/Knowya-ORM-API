package com.knowya.knowya.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "soporte")
public class Soporte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_support")
    private Integer idSupport;

    @Column(name = "nombre", length = 60, nullable = false)
    private String nombre;

    @Column(name = "email", length = 80, nullable = false)
    private String email;

    @Column(name = "telefono", length = 20)
    private String telefono;

    @OneToMany(mappedBy = "soporte")
    private List<ConsultaSoporte> consultas = new ArrayList<>();

    public Soporte() {
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
