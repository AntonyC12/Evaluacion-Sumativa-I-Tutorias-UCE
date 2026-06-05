package com.tutorias.domain.entities;

import com.tutorias.domain.enums.EstadoUsuario;
import com.tutorias.domain.enums.RolUsuario;
import com.tutorias.domain.valueobjects.CorreoInstitucional;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {
    private String id;
    private CorreoInstitucional correo;
    private String contrasenia;
    private RolUsuario rol;
    private EstadoUsuario estado;
    private String nombre;

    public boolean isActivo() {
        return this.estado == EstadoUsuario.ACTIVO;
    }

    public void activar() {
        this.estado = EstadoUsuario.ACTIVO;
    }

    public void desactivar() {
        this.estado = EstadoUsuario.INACTIVO;
    }
}
