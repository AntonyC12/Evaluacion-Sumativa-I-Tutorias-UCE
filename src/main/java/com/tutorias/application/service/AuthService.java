package com.tutorias.application.service;

import com.tutorias.domain.model.Usuario;
import com.tutorias.domain.valueobject.CorreoInstitucional;
import com.tutorias.infrastructure.repository.UsuarioRepository;
import com.tutorias.domain.exception.DomainException;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UsuarioRepository usuarioRepo;

    public AuthService(UsuarioRepository usuarioRepo) {
        this.usuarioRepo = usuarioRepo;
    }

    public Usuario login(String correo, String contrasena) {
        CorreoInstitucional correoInst = new CorreoInstitucional(correo);

        Usuario usuario = usuarioRepo.findByCorreoInstitucional(correoInst.value())
                .orElseThrow(() -> new DomainException("Credenciales incorrectas"));

        if (!usuario.getContrasena().equals(contrasena)) {
            throw new DomainException("Credenciales incorrectas");
        }

        usuario.verificarAcceso();

        return usuario;
    }
}
