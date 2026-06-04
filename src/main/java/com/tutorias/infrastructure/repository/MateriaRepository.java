package com.tutorias.infrastructure.repository;

import com.tutorias.domain.model.Materia;
import com.tutorias.infrastructure.repository.mongo.MongoMateriaRepository;
import com.tutorias.infrastructure.repository.mongo.mapper.PersistenciaMapper;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class MateriaRepository {
    private final MongoMateriaRepository mongoRepo;

    public MateriaRepository(MongoMateriaRepository mongoRepo) {
        this.mongoRepo = mongoRepo;
    }

    public Optional<Materia> findById(String id) {
        return mongoRepo.findById(id).map(PersistenciaMapper::toDomain);
    }

    public List<Materia> findAll() {
        return mongoRepo.findAll().stream()
                .map(PersistenciaMapper::toDomain)
                .collect(Collectors.toList());
    }

    public Materia save(Materia materia) {
        return PersistenciaMapper.toDomain(mongoRepo.save(PersistenciaMapper.toDocument(materia)));
    }

    public long count() {
        return mongoRepo.count();
    }
}
