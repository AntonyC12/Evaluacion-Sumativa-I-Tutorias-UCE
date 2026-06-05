package com.tutorias.domain.valueobjects;

import com.tutorias.domain.exceptions.DomainException;
import java.util.regex.Pattern;

public record CorreoInstitucional(String valor) {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

    public CorreoInstitucional {
        if (valor == null || valor.trim().isEmpty()) {
            throw new DomainException("El correo institucional no puede estar vacío.");
        }
        var matcher = EMAIL_PATTERN.matcher(valor);
        if (!matcher.matches()) {
            throw new DomainException("El formato del correo institucional es inválido.");
        }
        String domain = matcher.group(1).toLowerCase();
        if (!domain.equals("uce.edu.ec") && !domain.equals("universidad.edu.ec")) {
            throw new DomainException("El correo debe pertenecer al dominio de la universidad (uce.edu.ec o universidad.edu.ec).");
        }
    }
}
