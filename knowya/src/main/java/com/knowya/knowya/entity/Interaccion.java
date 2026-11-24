package com.knowya.knowya.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "interaccion")
public class Interaccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_interaccion")
    private Integer idInteraccion;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_user_origin", nullable = false)
    private Usuario usuarioOrigen;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_user_destiny", nullable = false)
    private Usuario usuarioDestino;

    @Column(name = "fecha_interaccion", nullable = false)
    private LocalDateTime fechaInteraccion;

    @Column(name = "estado", length = 20, nullable = false)
    private String estado;

    public Interaccion() {
    }

    public Integer getIdInteraccion() {
        return idInteraccion;
    }

    public void setIdInteraccion(Integer idInteraccion) {
        this.idInteraccion = idInteraccion;
    }

    public Usuario getUsuarioOrigen() {
        return usuarioOrigen;
    }

    public void setUsuarioOrigen(Usuario usuarioOrigen) {
        this.usuarioOrigen = usuarioOrigen;
    }

    public Usuario getUsuarioDestino() {
        return usuarioDestino;
    }

    public void setUsuarioDestino(Usuario usuarioDestino) {
        this.usuarioDestino = usuarioDestino;
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
