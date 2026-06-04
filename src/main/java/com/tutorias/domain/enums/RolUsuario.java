package com.tutorias.domain.enums;

import com.tutorias.domain.exception.DomainException;

public enum RolUsuario {
    ADMINISTRADOR("administrador"),
    DOCENTE("docente"),
    ESTUDIANTE("estudiante");

    private final String valor;

    RolUsuario(String valor) {
        this.valor = valor;
    }

    public String getValor() {
        return valor;
    }

    public static RolUsuario fromString(String text) {
        if (text == null) {
            throw new DomainException("El rol de usuario no puede ser nulo");
        }
        for (RolUsuario rol : RolUsuario.values()) {
            if (rol.valor.equalsIgnoreCase(text)) {
                return rol;
            }
        }
        throw new DomainException("Rol de usuario inválido: " + text);
    }
}
