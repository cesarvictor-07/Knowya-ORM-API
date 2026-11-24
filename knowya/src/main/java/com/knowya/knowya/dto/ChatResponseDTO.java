package com.knowya.knowya.dto;

public class ChatResponseDTO {
    private Integer idChat;
    private Integer idOtroUsuario;
    private String userName;
    private Boolean activo;

    public ChatResponseDTO(Integer idChat, Integer idOtroUsuario, String userName, Boolean activo) {
        this.idChat = idChat;
        this.idOtroUsuario = idOtroUsuario;
        this.userName = userName;
        this.activo = activo;
    }

    public Integer getIdChat() {
        return idChat;
    }

    public Integer getIdOtroUsuario() {
        return idOtroUsuario;
    }

    public String getUserName() {
        return userName;
    }

    public Boolean getActivo() {
        return activo;
    }
}
