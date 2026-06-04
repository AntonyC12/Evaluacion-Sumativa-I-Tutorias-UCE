package com.tutorias.infrastructure.repository;

import com.tutorias.domain.model.HistorialTutoria;
import com.tutorias.infrastructure.repository.mongo.MongoHistorialTutoriaRepository;
import com.tutorias.infrastructure.repository.mongo.mapper.PersistenciaMapper;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class HistorialRepository {
    private final MongoHistorialTutoriaRepository mongoRepo;

    public HistorialRepository(MongoHistorialTutoriaRepository mongoRepo) {
        this.mongoRepo = mongoRepo;
    }

    public List<HistorialTutoria> findByTutoriaId(String tutoriaId) {
        return mongoRepo.findByTutoriaId(tutoriaId).stream()
                .map(PersistenciaMapper::toDomain)
                .collect(Collectors.toList());
    }

    public HistorialTutoria save(HistorialTutoria historial) {
        return PersistenciaMapper.toDomain(mongoRepo.save(PersistenciaMapper.toDocument(historial)));
    }

    public long count() {
        return mongoRepo.count();
    }
}
