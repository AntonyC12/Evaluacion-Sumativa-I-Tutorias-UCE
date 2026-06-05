package com.tutorias.domain.valueobjects;

import com.tutorias.domain.exceptions.DomainException;

public record MotivoTutoria(String valor) {
    public MotivoTutoria {
        if (valor == null || valor.trim().isEmpty()) {
            throw new DomainException("El motivo de la tutoría no puede estar vacío.");
        }
        if (valor.length() < 5 || valor.length() > 200) {
            throw new DomainException("El motivo de la tutoría debe tener entre 5 y 200 caracteres.");
        }
    }
}
