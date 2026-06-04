package com.tutorias.infrastructure.repository;

import com.tutorias.domain.model.Usuario;
import com.tutorias.domain.model.Estudiante;
import com.tutorias.domain.model.Docente;
import com.tutorias.infrastructure.repository.mongo.MongoUsuarioRepository;
import com.tutorias.infrastructure.repository.mongo.MongoEstudianteRepository;
import com.tutorias.infrastructure.repository.mongo.MongoDocenteRepository;
import com.tutorias.infrastructure.repository.mongo.mapper.PersistenciaMapper;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class UsuarioRepository {
    private final MongoUsuarioRepository mongoUserRepo;
    private final MongoEstudianteRepository mongoEstudianteRepo;
    private final MongoDocenteRepository mongoDocenteRepo;

    public UsuarioRepository(MongoUsuarioRepository mongoUserRepo,
                             MongoEstudianteRepository mongoEstudianteRepo,
                             MongoDocenteRepository mongoDocenteRepo) {
        this.mongoUserRepo = mongoUserRepo;
        this.mongoEstudianteRepo = mongoEstudianteRepo;
        this.mongoDocenteRepo = mongoDocenteRepo;
    }

    // Usuario CRUD
    public Optional<Usuario> findById(String id) {
        return mongoUserRepo.findById(id).map(PersistenciaMapper::toDomain);
    }

    public Optional<Usuario> findByCorreoInstitucional(String correo) {
        return mongoUserRepo.findByCorreoInstitucional(correo).map(PersistenciaMapper::toDomain);
    }

    public List<Usuario> findByRol(String rol) {
        return mongoUserRepo.findByRol(rol).stream()
                .map(PersistenciaMapper::toDomain)
                .collect(Collectors.toList());
    }

    public List<Usuario> findAll() {
        return mongoUserRepo.findAll().stream()
                .map(PersistenciaMapper::toDomain)
                .collect(Collectors.toList());
    }

    public Usuario save(Usuario usuario) {
        return PersistenciaMapper.toDomain(mongoUserRepo.save(PersistenciaMapper.toDocument(usuario)));
    }

    public long count() {
        return mongoUserRepo.count();
    }

    // Estudiante CRUD (consolidated)
    public Optional<Estudiante> findEstudianteById(String id) {
        return mongoEstudianteRepo.findById(id).map(PersistenciaMapper::toDomain);
    }

    public Optional<Estudiante> findEstudianteByUsuarioId(String usuarioId) {
        return mongoEstudianteRepo.findByUsuarioId(usuarioId).map(PersistenciaMapper::toDomain);
    }

    public Estudiante saveEstudiante(Estudiante estudiante) {
        return PersistenciaMapper.toDomain(mongoEstudianteRepo.save(PersistenciaMapper.toDocument(estudiante)));
    }

    public long countEstudiantes() {
        return mongoEstudianteRepo.count();
    }

    // Docente CRUD (consolidated)
    public Optional<Docente> findDocenteById(String id) {
        return mongoDocenteRepo.findById(id).map(PersistenciaMapper::toDomain);
    }

    public Optional<Docente> findDocenteByUsuarioId(String usuarioId) {
        return mongoDocenteRepo.findByUsuarioId(usuarioId).map(PersistenciaMapper::toDomain);
    }

    public Docente saveDocente(Docente docente) {
        return PersistenciaMapper.toDomain(mongoDocenteRepo.save(PersistenciaMapper.toDocument(docente)));
    }

    public long countDocentes() {
        return mongoDocenteRepo.count();
    }
}
