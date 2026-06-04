package com.tutorias.infrastructure.repository;

import com.tutorias.domain.model.Disponibilidad;
import com.tutorias.infrastructure.repository.mongo.MongoDisponibilidadRepository;
import com.tutorias.infrastructure.repository.mongo.document.DisponibilidadDocument;
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
public class DisponibilidadRepository {
    private final MongoDisponibilidadRepository mongoRepo;
    private final MongoTemplate mongoTemplate;

    public DisponibilidadRepository(MongoDisponibilidadRepository mongoRepo, MongoTemplate mongoTemplate) {
        this.mongoRepo = mongoRepo;
        this.mongoTemplate = mongoTemplate;
    }

    public Optional<Disponibilidad> findById(String id) {
        return mongoRepo.findById(id).map(PersistenciaMapper::toDomain);
    }

    public List<Disponibilidad> findByDocenteIdAndFechaAndEstado(String docenteId, String fecha, String estado) {
        return mongoRepo.findByDocenteIdAndFechaAndEstado(docenteId, fecha, estado).stream()
                .map(PersistenciaMapper::toDomain)
                .collect(Collectors.toList());
    }

    public List<Disponibilidad> findByFiltros(String estado, String docenteId, String materiaId, String fecha) {
        Query query = new Query();
        List<Criteria> criteriaList = new ArrayList<>();

        if (estado != null && !estado.isEmpty()) {
            criteriaList.add(Criteria.where("estado").is(estado));
        }
        if (docenteId != null && !docenteId.isEmpty()) {
            criteriaList.add(Criteria.where("docenteId").is(docenteId));
        }
        if (materiaId != null && !materiaId.isEmpty()) {
            criteriaList.add(Criteria.where("materiaId").is(materiaId));
        }
        if (fecha != null && !fecha.isEmpty()) {
            criteriaList.add(Criteria.where("fecha").is(fecha));
        }

        if (!criteriaList.isEmpty()) {
            query.addCriteria(new Criteria().andOperator(criteriaList.toArray(new Criteria[0])));
        }

        return mongoTemplate.find(query, DisponibilidadDocument.class).stream()
                .map(PersistenciaMapper::toDomain)
                .collect(Collectors.toList());
    }

    public Disponibilidad save(Disponibilidad disp) {
        return PersistenciaMapper.toDomain(mongoRepo.save(PersistenciaMapper.toDocument(disp)));
    }

    public void deleteById(String id) {
        mongoRepo.deleteById(id);
    }

    public long count() {
        return mongoRepo.count();
    }
}
