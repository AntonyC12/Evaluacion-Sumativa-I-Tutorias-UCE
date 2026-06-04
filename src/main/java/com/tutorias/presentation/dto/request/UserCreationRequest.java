package com.tutorias.presentation.dto.request;

public record UserCreationRequest(
    String nombre,
    String apellido,
    String correoInstitucional,
    String contrasena,
    String rol,
    String estado,
    String carrera,
    String nivel,
    String paralelo,
    String departamento
) {}
