package com.tutorias.application.servicios;

import com.tutorias.domain.entities.Usuario;
import com.tutorias.domain.exceptions.DomainException;
import com.tutorias.domain.repositorios.UsuarioRepository;
import com.tutorias.domain.valueobjects.CorreoInstitucional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioApplicationService {
    private final UsuarioRepository usuarioRepository;

    public Usuario autenticar(String email, String contrasenia) {
        CorreoInstitucional correo = new CorreoInstitucional(email);
        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new DomainException("Credenciales inválidas."));

        if (!BCrypt.checkpw(contrasenia, usuario.getContrasenia())) {
            throw new DomainException("Credenciales inválidas.");
        }

        if (!usuario.isActivo()) {
            throw new DomainException("El usuario está inactivo. Contacte al administrador.");
        }

        return usuario;
    }

    public void cambiarEstado(String adminUserId, String targetUserId, boolean activar) {
        Usuario admin = usuarioRepository.findById(adminUserId)
                .orElseThrow(() -> new DomainException("Administrador no encontrado."));

        if (admin.getRol() != com.tutorias.domain.enums.RolUsuario.ADMINISTRADOR) {
            throw new DomainException("Solo el administrador puede activar o desactivar cuentas.");
        }

        Usuario target = usuarioRepository.findById(targetUserId)
                .orElseThrow(() -> new DomainException("Usuario no encontrado."));

        if (activar) {
            target.activar();
        } else {
            target.desactivar();
        }

        usuarioRepository.save(target);
    }

    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    public Usuario buscarPorId(String id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new DomainException("Usuario no encontrado."));
    }
}
