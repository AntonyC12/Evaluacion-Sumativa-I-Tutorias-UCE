package com.tutorias.presentation.dto;

import com.tutorias.domain.enums.RolUsuario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
    private String id;
    private String correo;
    private RolUsuario rol;
    private String nombre;
}
