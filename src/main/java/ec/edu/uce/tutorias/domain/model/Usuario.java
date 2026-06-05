package ec.edu.uce.tutorias.domain.model;

import ec.edu.uce.tutorias.domain.vo.CorreoInstitucional;
import ec.edu.uce.tutorias.domain.vo.Rol;
import ec.edu.uce.tutorias.domain.vo.EstadoUsuario;
import ec.edu.uce.tutorias.domain.exception.DomainException;

public class Usuario {

    private final String id;
    private final CorreoInstitucional correo;
    private final Rol rol;
    private EstadoUsuario estado;

    public Usuario(String id, CorreoInstitucional correo, Rol rol) {
        if (id == null || id.trim().isEmpty()) {
            throw new DomainException("El ID del usuario es obligatorio");
        }
        if (correo == null) {
            throw new DomainException("El correo es obligatorio");
        }
        if (rol == null) {
            throw new DomainException("El rol es obligatorio");
        }
        this.id = id;
        this.correo = correo;
        this.rol = rol;
        this.estado = EstadoUsuario.ACTIVO;
    }

    // Constructor para rehidratación
    public Usuario(String id, CorreoInstitucional correo, Rol rol, EstadoUsuario estado) {
        if (id == null || id.trim().isEmpty()) {
            throw new DomainException("El ID del usuario es obligatorio");
        }
        if (correo == null) {
            throw new DomainException("El correo es obligatorio");
        }
        if (rol == null) {
            throw new DomainException("El rol es obligatorio");
        }
        if (estado == null) {
            throw new DomainException("El estado del usuario es obligatorio");
        }
        this.id = id;
        this.correo = correo;
        this.rol = rol;
        this.estado = estado;
    }

    public void desactivar() {
        this.estado = EstadoUsuario.INACTIVO;
    }

    public void activar() {
        this.estado = EstadoUsuario.ACTIVO;
    }

    public String getId() {
        return id;
    }

    public CorreoInstitucional getCorreo() {
        return correo;
    }

    public Rol getRol() {
        return rol;
    }

    public EstadoUsuario getEstado() {
        return estado;
    }

    public boolean isActivo() {
        return this.estado == EstadoUsuario.ACTIVO;
    }
}
