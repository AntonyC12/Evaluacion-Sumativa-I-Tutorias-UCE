package ec.edu.uce.tutorias.infrastructure.persistence.adapter;

import ec.edu.uce.tutorias.domain.model.Tutoria;
import ec.edu.uce.tutorias.domain.repository.TutoriaRepository;
import ec.edu.uce.tutorias.domain.vo.EstadoTutoria;
import ec.edu.uce.tutorias.domain.vo.MotivoTutoria;
import ec.edu.uce.tutorias.infrastructure.persistence.document.TutoriaDocument;
import ec.edu.uce.tutorias.infrastructure.persistence.repository.SpringDataTutoriaMongoRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class TutoriaMongoRepositoryAdapter implements TutoriaRepository {

    private final SpringDataTutoriaMongoRepository mongoRepository;

    public TutoriaMongoRepositoryAdapter(SpringDataTutoriaMongoRepository mongoRepository) {
        this.mongoRepository = mongoRepository;
    }

    @Override
    public void guardar(Tutoria tutoria) {
        TutoriaDocument doc = new TutoriaDocument();
        doc.setId(tutoria.getId());
        doc.setEstudianteId(tutoria.getEstudianteId());
        doc.setDocenteId(tutoria.getDocenteId());
        doc.setMateriaId(tutoria.getMateriaId());
        doc.setDisponibilidadId(tutoria.getDisponibilidadId());
        doc.setFechaHoraInicio(tutoria.getFechaHoraInicio());
        doc.setFechaHoraFin(tutoria.getFechaHoraFin());
        doc.setMotivo(tutoria.getMotivo().getValor());
        doc.setEstado(tutoria.getEstado().name());
        
        mongoRepository.save(doc);
    }

    @Override
    public Optional<Tutoria> buscarPorId(String id) {
        return mongoRepository.findById(id).map(this::toDomain);
    }

    @Override
    public List<Tutoria> buscarPorEstudianteYDocenteYMateriaEnRango(String estudianteId, String docenteId, String materiaId, LocalDateTime inicio, LocalDateTime fin) {
        return mongoRepository.findTutoriasMismaSemana(estudianteId, docenteId, materiaId, inicio, fin)
                .stream().map(this::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<Tutoria> buscarSolapadasParaEstudiante(String estudianteId, LocalDateTime inicio, LocalDateTime fin) {
        return mongoRepository.findSolapadasEstudiante(estudianteId, inicio, fin)
                .stream().map(this::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<Tutoria> buscarSolapadasParaDocente(String docenteId, LocalDateTime inicio, LocalDateTime fin) {
        return mongoRepository.findSolapadasDocente(docenteId, inicio, fin)
                .stream().map(this::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<Tutoria> buscarTodas() {
        return mongoRepository.findAll()
                .stream().map(this::toDomain).collect(Collectors.toList());
    }

    private Tutoria toDomain(TutoriaDocument doc) {
        return new Tutoria(
                doc.getId(),
                doc.getEstudianteId(),
                doc.getDocenteId(),
                doc.getMateriaId(),
                doc.getDisponibilidadId(),
                doc.getFechaHoraInicio(),
                doc.getFechaHoraFin(),
                new MotivoTutoria(doc.getMotivo()),
                EstadoTutoria.valueOf(doc.getEstado())
        );
    }
}
