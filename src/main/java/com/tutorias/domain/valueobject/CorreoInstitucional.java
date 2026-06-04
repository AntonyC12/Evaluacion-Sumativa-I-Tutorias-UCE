package com.tutorias.domain.valueobject;

import com.tutorias.domain.exception.DomainException;

public record CorreoInstitucional(String value) {
    public CorreoInstitucional {
        if (value == null || value.trim().isEmpty()) {
            throw new DomainException("El correo institucional no puede estar vacío");
        }
        if (!value.endsWith("@uce.edu.ec")) {
            throw new DomainException("El correo debe ser institucional (@uce.edu.ec)");
        }
    }
}
