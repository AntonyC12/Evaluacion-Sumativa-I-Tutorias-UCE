package ec.edu.uce.tutorias.application.usecase;

import ec.edu.uce.tutorias.domain.model.Usuario;
import ec.edu.uce.tutorias.domain.repository.UsuarioRepository;
import ec.edu.uce.tutorias.domain.vo.CorreoInstitucional;
import ec.edu.uce.tutorias.domain.vo.Rol;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class RegistrarUsuarioUseCase {

    private final UsuarioRepository usuarioRepository;

    public RegistrarUsuarioUseCase(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public String ejecutar(String id, String correo, Rol rol) {
        String finalId = (id == null || id.trim().isEmpty()) ? UUID.randomUUID().toString() : id;
        Usuario usuario = new Usuario(finalId, new CorreoInstitucional(correo), rol);
        usuarioRepository.guardar(usuario);
        return finalId;
    }
}
