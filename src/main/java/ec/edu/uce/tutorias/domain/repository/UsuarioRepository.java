package ec.edu.uce.tutorias.domain.repository;

import ec.edu.uce.tutorias.domain.model.Usuario;
import java.util.List;
import java.util.Optional;

public interface UsuarioRepository {
    void guardar(Usuario usuario);
    Optional<Usuario> buscarPorId(String id);
    Optional<Usuario> buscarPorCorreo(String correo);
    List<Usuario> buscarTodos();
}
