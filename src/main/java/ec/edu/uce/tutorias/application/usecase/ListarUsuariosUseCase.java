package ec.edu.uce.tutorias.application.usecase;

import ec.edu.uce.tutorias.domain.model.Usuario;
import ec.edu.uce.tutorias.domain.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListarUsuariosUseCase {

    private final UsuarioRepository usuarioRepository;

    public ListarUsuariosUseCase(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public List<Usuario> ejecutar() {
        return usuarioRepository.buscarTodos();
    }
}
