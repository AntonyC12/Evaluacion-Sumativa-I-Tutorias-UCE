package com.tutorias.infrastructure.repository;

import com.tutorias.domain.model.Tutoria;
import com.tutorias.infrastructure.repository.mongo.MongoTutoriaRepository;
import com.tutorias.infrastructure.repository.mongo.document.TutoriaDocument;
import com.tutorias.infrastructure.repository.mongo.mapper.PersistenciaMapper;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class TutoriaRepository {
    private final MongoTutoriaRepository mongoRepo;
    private final MongoTemplate mongoTemplate;

    public TutoriaRepository(MongoTutoriaRepository mongoRepo, MongoTemplate mongoTemplate) {
        this.mongoRepo = mongoRepo;
        this.mongoTemplate = mongoTemplate;
    }

    public Optional<Tutoria> findById(String id) {
        return mongoRepo.findById(id).map(PersistenciaMapper::toDomain);
    }

    public List<Tutoria> findByEstudianteId(String estudianteId) {
        return mongoRepo.findByEstudianteId(estudianteId).stream()
                .map(PersistenciaMapper::toDomain)
                .collect(Collectors.toList());
    }

    public List<Tutoria> findByDocenteId(String docenteId) {
        return mongoRepo.findByDocenteId(docenteId).stream()
                .map(PersistenciaMapper::toDomain)
                .collect(Collectors.toList());
    }

    public List<Tutoria> findAll() {
        return mongoRepo.findAll().stream()
                .map(PersistenciaMapper::toDomain)
                .collect(Collectors.toList());
    }

    public List<Tutoria> findByEstudianteIdAndDocenteIdAndMateriaIdAndEstadoNot(String estudianteId, String docenteId, String materiaId, String estadoExcluido) {
        return mongoRepo.findByEstudianteIdAndDocenteIdAndMateriaIdAndEstadoNot(estudianteId, docenteId, materiaId, estadoExcluido).stream()
                .map(PersistenciaMapper::toDomain)
                .collect(Collectors.toList());
    }

    public List<Tutoria> findByEstudianteIdAndEstadoIn(String estudianteId, List<String> estadosActivos) {
        return mongoRepo.findByEstudianteIdAndEstadoIn(estudianteId, estadosActivos).stream()
                .map(PersistenciaMapper::toDomain)
                .collect(Collectors.toList());
    }

    public List<Tutoria> findByFiltros(String estudianteId, String docenteId, String estado, String estudianteNombre, String docenteNombre) {
        Query query = new Query();
        List<Criteria> criteriaList = new ArrayList<>();

        if (estudianteId != null && !estudianteId.isEmpty()) {
            criteriaList.add(Criteria.where("estudianteId").is(estudianteId));
        }
        if (docenteId != null && !docenteId.isEmpty()) {
            criteriaList.add(Criteria.where("docenteId").is(docenteId));
        }
        if (estado != null && !estado.isEmpty()) {
            criteriaList.add(Criteria.where("estado").is(estado));
        }
        if (estudianteNombre != null && !estudianteNombre.isEmpty()) {
            criteriaList.add(Criteria.where("estudianteNombre").regex(estudianteNombre, "i"));
        }
        if (docenteNombre != null && !docenteNombre.isEmpty()) {
            criteriaList.add(Criteria.where("docenteNombre").regex(docenteNombre, "i"));
        }

        if (!criteriaList.isEmpty()) {
            query.addCriteria(new Criteria().andOperator(criteriaList.toArray(new Criteria[0])));
        }

        return mongoTemplate.find(query, TutoriaDocument.class).stream()
                .map(PersistenciaMapper::toDomain)
                .collect(Collectors.toList());
    }

    public Tutoria save(Tutoria tutoria) {
        return PersistenciaMapper.toDomain(mongoRepo.save(PersistenciaMapper.toDocument(tutoria)));
    }

    public long count() {
        return mongoRepo.count();
    }
}
