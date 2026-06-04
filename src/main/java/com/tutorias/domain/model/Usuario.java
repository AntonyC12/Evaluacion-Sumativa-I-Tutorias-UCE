package com.tutorias.domain.model;

import com.tutorias.domain.valueobject.CorreoInstitucional;
import com.tutorias.domain.enums.EstadoUsuario;
import com.tutorias.domain.enums.RolUsuario;
import com.tutorias.domain.exception.DomainException;
import java.time.LocalDateTime;

public class Usuario {
    private String id;
    private String nombre;
    private String apellido;
    private CorreoInstitucional correoInstitucional;
    private String contrasena;
    private RolUsuario rol;
    private EstadoUsuario estado;
    private LocalDateTime fechaCreacion;

    public Usuario() {}

    public Usuario(String id, String nombre, String apellido, CorreoInstitucional correoInstitucional, 
                   String contrasena, RolUsuario rol, EstadoUsuario estado, LocalDateTime fechaCreacion) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.correoInstitucional = correoInstitucional;
        this.contrasena = contrasena;
        this.rol = rol;
        this.estado = estado;
        this.fechaCreacion = fechaCreacion;
        validate();
    }

    public void validate() {
        if (nombre == null || nombre.trim().isEmpty() ||
            apellido == null || apellido.trim().isEmpty()) {
            throw new DomainException("El nombre y el apellido son obligatorios");
        }
        if (rol == null) {
            throw new DomainException("El rol del usuario es obligatorio");
        }
        if (estado == null) {
            throw new DomainException("El estado es obligatorio");
        }
    }

    public void verificarAcceso() {
        if (estado == EstadoUsuario.INACTIVO) {
            throw new DomainException("No se permite el acceso a cuentas inactivas");
        }
    }

    public void toggleEstado() {
        this.estado = (this.estado == EstadoUsuario.ACTIVO) ? EstadoUsuario.INACTIVO : EstadoUsuario.ACTIVO;
    }

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    
    public CorreoInstitucional getCorreoInstitucional() { return correoInstitucional; }
    public void setCorreoInstitucional(CorreoInstitucional correoInstitucional) { this.correoInstitucional = correoInstitucional; }
    
    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }
    
    public RolUsuario getRol() { return rol; }
    public void setRol(RolUsuario rol) { this.rol = rol; }
    
    public EstadoUsuario getEstado() { return estado; }
    public void setEstado(EstadoUsuario estado) { this.estado = estado; }
    
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
}
