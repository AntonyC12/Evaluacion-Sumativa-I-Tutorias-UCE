package com.tutorias.domain.valueobject;

import com.tutorias.domain.exception.DomainException;

public record MotivoTutoria(String value) {
    public MotivoTutoria {
        if (value == null || value.trim().isEmpty()) {
            throw new DomainException("El motivo de la tutoría es obligatorio");
        }
    }
}
