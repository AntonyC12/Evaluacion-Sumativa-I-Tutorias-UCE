package com.tutorias.domain.repositorios;

import com.tutorias.domain.entities.Usuario;
import com.tutorias.domain.valueobjects.CorreoInstitucional;
import java.util.List;
import java.util.Optional;

public interface UsuarioRepository {
    Usuario save(Usuario usuario);
    Optional<Usuario> findById(String id);
    Optional<Usuario> findByCorreo(CorreoInstitucional correo);
    List<Usuario> findAll();
}
