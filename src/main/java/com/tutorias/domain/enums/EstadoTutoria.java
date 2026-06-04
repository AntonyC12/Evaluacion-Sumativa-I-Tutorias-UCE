package com.tutorias.domain.enums;

import com.tutorias.domain.exception.DomainException;

public enum EstadoTutoria {
    PENDIENTE("Pendiente"),
    CONFIRMADA("Confirmada"),
    ASISTIDA("Asistida"),
    INASISTENCIA("Inasistencia"),
    CANCELADA("Cancelada");

    private final String valor;

    EstadoTutoria(String valor) {
        this.valor = valor;
    }

    public String getValor() {
        return valor;
    }

    public static EstadoTutoria fromString(String text) {
        if (text == null) {
            throw new DomainException("El estado de tutoría no puede ser nulo");
        }
        for (EstadoTutoria estado : EstadoTutoria.values()) {
            if (estado.valor.equalsIgnoreCase(text)) {
                return estado;
            }
        }
        throw new DomainException("Estado de tutoría inválido: " + text);
    }
}
