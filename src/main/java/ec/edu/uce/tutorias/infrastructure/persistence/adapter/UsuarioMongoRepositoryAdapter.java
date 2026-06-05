package ec.edu.uce.tutorias.infrastructure.persistence.adapter;

import ec.edu.uce.tutorias.domain.model.Usuario;
import ec.edu.uce.tutorias.domain.repository.UsuarioRepository;
import ec.edu.uce.tutorias.domain.vo.CorreoInstitucional;
import ec.edu.uce.tutorias.domain.vo.EstadoUsuario;
import ec.edu.uce.tutorias.domain.vo.Rol;
import ec.edu.uce.tutorias.infrastructure.persistence.document.UsuarioDocument;
import ec.edu.uce.tutorias.infrastructure.persistence.repository.SpringDataUsuarioMongoRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class UsuarioMongoRepositoryAdapter implements UsuarioRepository {

    private final SpringDataUsuarioMongoRepository mongoRepository;

    public UsuarioMongoRepositoryAdapter(SpringDataUsuarioMongoRepository mongoRepository) {
        this.mongoRepository = mongoRepository;
    }

    @Override
    public void guardar(Usuario usuario) {
        UsuarioDocument doc = new UsuarioDocument();
        doc.setId(usuario.getId());
        doc.setCorreo(usuario.getCorreo().getValor());
        doc.setRol(usuario.getRol().name());
        doc.setActivo(usuario.getEstado() == EstadoUsuario.ACTIVO);
        mongoRepository.save(doc);
    }

    @Override
    public Optional<Usuario> buscarPorId(String id) {
        return mongoRepository.findById(id).map(this::toDomain);
    }

    @Override
    public Optional<Usuario> buscarPorCorreo(String correo) {
        return mongoRepository.findByCorreo(correo).map(this::toDomain);
    }

    @Override
    public List<Usuario> buscarTodos() {
        return mongoRepository.findAll()
                .stream().map(this::toDomain).collect(Collectors.toList());
    }

    private Usuario toDomain(UsuarioDocument doc) {
        EstadoUsuario estado = doc.isActivo() ? EstadoUsuario.ACTIVO : EstadoUsuario.INACTIVO;
        return new Usuario(
                doc.getId(),
                new CorreoInstitucional(doc.getCorreo()),
                Rol.valueOf(doc.getRol()),
                estado
        );
    }
}
