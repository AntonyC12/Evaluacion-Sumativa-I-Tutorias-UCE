package com.tutorias.presentation.dto.response;

public record UsuarioResponse(
    String id,
    String nombre,
    String apellido,
    String correoInstitucional,
    String rol,
    String estado,
    String fechaCreacion
) {}
