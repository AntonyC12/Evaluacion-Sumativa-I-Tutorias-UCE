package com.tutorias.application.service;

import com.tutorias.domain.model.Usuario;
import com.tutorias.domain.model.Estudiante;
import com.tutorias.domain.model.Docente;
import com.tutorias.domain.valueobject.CorreoInstitucional;
import com.tutorias.domain.enums.EstadoUsuario;
import com.tutorias.domain.enums.RolUsuario;
import com.tutorias.infrastructure.repository.UsuarioRepository;
import com.tutorias.domain.exception.DomainException;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepo;

    public UsuarioService(UsuarioRepository usuarioRepo) {
        this.usuarioRepo = usuarioRepo;
    }

    public List<Usuario> listarUsuarios(String rol) {
        if (rol != null && !rol.trim().isEmpty()) {
            return usuarioRepo.findByRol(rol);
        }
        return usuarioRepo.findAll();
    }

    public Usuario crearUsuario(String nombre, String apellido, String correo, String contrasena,
                                 String rol, String estado, String carrera, String nivel, String paralelo,
                                 String departamento) {
        CorreoInstitucional correoInst = new CorreoInstitucional(correo);

        if (usuarioRepo.findByCorreoInstitucional(correoInst.value()).isPresent()) {
            throw new DomainException("El correo ya esta registrado");
        }

        String userId = UUID.randomUUID().toString();
        Usuario usuario = new Usuario(
                userId,
                nombre,
                apellido,
                correoInst,
                contrasena,
                RolUsuario.fromString(rol),
                EstadoUsuario.fromString(estado),
                LocalDateTime.now()
        );

        Usuario savedUser = usuarioRepo.save(usuario);

        if ("estudiante".equals(rol)) {
            Estudiante estudiante = new Estudiante(
                    UUID.randomUUID().toString(),
                    userId,
                    carrera,
                    nivel,
                    paralelo
            );
            usuarioRepo.saveEstudiante(estudiante);
        } else if ("docente".equals(rol)) {
            Docente docente = new Docente(
                    UUID.randomUUID().toString(),
                    userId,
                    departamento
            );
            usuarioRepo.saveDocente(docente);
        }

        return savedUser;
    }

    public Usuario toggleEstado(String id) {
        Usuario usuario = usuarioRepo.findById(id)
                .orElseThrow(() -> new DomainException("Usuario no encontrado"));
        usuario.toggleEstado();
        return usuarioRepo.save(usuario);
    }

    public Estudiante obtenerEstudiantePorUsuarioId(String usuarioId) {
        return usuarioRepo.findEstudianteByUsuarioId(usuarioId)
                .orElseThrow(() -> new DomainException("Estudiante no encontrado"));
    }

    public Docente obtenerDocentePorUsuarioId(String usuarioId) {
        return usuarioRepo.findDocenteByUsuarioId(usuarioId)
                .orElseThrow(() -> new DomainException("Docente no encontrado"));
    }
}
