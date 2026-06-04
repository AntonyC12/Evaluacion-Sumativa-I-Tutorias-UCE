package com.tutorias.domain.enums;

import com.tutorias.domain.exception.DomainException;

public enum EstadoUsuario {
    ACTIVO("Activo"),
    INACTIVO("Inactivo");

    private final String valor;

    EstadoUsuario(String valor) {
        this.valor = valor;
    }

    public String getValor() {
        return valor;
    }

    public static EstadoUsuario fromString(String text) {
        if (text == null) {
            throw new DomainException("El estado del usuario no puede ser nulo");
        }
        for (EstadoUsuario estado : EstadoUsuario.values()) {
            if (estado.valor.equalsIgnoreCase(text)) {
                return estado;
            }
        }
        throw new DomainException("Estado de usuario inválido: " + text);
    }
}
