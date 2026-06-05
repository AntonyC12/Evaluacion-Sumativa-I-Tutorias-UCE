package ec.edu.uce.tutorias.infrastructure.persistence.adapter;

import ec.edu.uce.tutorias.domain.model.Materia;
import ec.edu.uce.tutorias.domain.repository.MateriaRepository;
import ec.edu.uce.tutorias.infrastructure.persistence.document.MateriaDocument;
import ec.edu.uce.tutorias.infrastructure.persistence.repository.SpringDataMateriaMongoRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class MateriaMongoRepositoryAdapter implements MateriaRepository {

    private final SpringDataMateriaMongoRepository mongoRepository;

    public MateriaMongoRepositoryAdapter(SpringDataMateriaMongoRepository mongoRepository) {
        this.mongoRepository = mongoRepository;
    }

    @Override
    public void guardar(Materia materia) {
        MateriaDocument doc = new MateriaDocument();
        doc.setId(materia.getId());
        doc.setNombre(materia.getNombre());
        mongoRepository.save(doc);
    }

    @Override
    public Optional<Materia> buscarPorId(String id) {
        return mongoRepository.findById(id).map(this::toDomain);
    }

    @Override
    public List<Materia> buscarTodas() {
        return mongoRepository.findAll()
                .stream().map(this::toDomain).collect(Collectors.toList());
    }

    private Materia toDomain(MateriaDocument doc) {
        return new Materia(doc.getId(), doc.getNombre());
    }
}
