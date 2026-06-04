package com.tutorias.presentation.mapper;

import com.tutorias.domain.model.Usuario;
import com.tutorias.presentation.dto.response.UsuarioResponse;
import java.time.format.DateTimeFormatter;

public class UsuarioMapper {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static UsuarioResponse toResponse(Usuario u) {
        if (u == null) return null;
        String fecha = u.getFechaCreacion() != null ? u.getFechaCreacion().format(FORMATTER) : null;
        return new UsuarioResponse(
            u.getId(),
            u.getNombre(),
            u.getApellido(),
            u.getCorreoInstitucional().value(),
            u.getRol().getValor(),
            u.getEstado().getValor(),
            fecha
        );
    }
}
