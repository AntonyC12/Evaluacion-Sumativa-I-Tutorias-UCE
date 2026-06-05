package ec.edu.uce.tutorias.infrastructure.persistence.adapter;

import ec.edu.uce.tutorias.domain.model.Disponibilidad;
import ec.edu.uce.tutorias.domain.repository.DisponibilidadRepository;
import ec.edu.uce.tutorias.domain.vo.RangoHorario;
import ec.edu.uce.tutorias.infrastructure.persistence.document.DisponibilidadDocument;
import ec.edu.uce.tutorias.infrastructure.persistence.repository.SpringDataDisponibilidadMongoRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class DisponibilidadMongoRepositoryAdapter implements DisponibilidadRepository {

    private final SpringDataDisponibilidadMongoRepository mongoRepository;

    public DisponibilidadMongoRepositoryAdapter(SpringDataDisponibilidadMongoRepository mongoRepository) {
        this.mongoRepository = mongoRepository;
    }

    @Override
    public void guardar(Disponibilidad disponibilidad) {
        DisponibilidadDocument doc = new DisponibilidadDocument();
        doc.setId(disponibilidad.getId());
        doc.setDocenteId(disponibilidad.getDocenteId());
        doc.setMateriaId(disponibilidad.getMateriaId());
        doc.setFecha(disponibilidad.getFecha());
        doc.setHoraInicio(disponibilidad.getRangoHorario().getHoraInicio());
        doc.setHoraFin(disponibilidad.getRangoHorario().getHoraFin());
        doc.setReservado(disponibilidad.isReservado());
        mongoRepository.save(doc);
    }

    @Override
    public Optional<Disponibilidad> buscarPorId(String id) {
        return mongoRepository.findById(id).map(this::toDomain);
    }

    @Override
    public List<Disponibilidad> buscarPorDocenteYFecha(String docenteId, LocalDate fecha) {
        return mongoRepository.findByDocenteIdAndFecha(docenteId, fecha)
                .stream().map(this::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<Disponibilidad> buscarTodas() {
        return mongoRepository.findAll()
                .stream().map(this::toDomain).collect(Collectors.toList());
    }

    private Disponibilidad toDomain(DisponibilidadDocument doc) {
        return new Disponibilidad(
                doc.getId(),
                doc.getDocenteId(),
                doc.getMateriaId(),
                doc.getFecha(),
                new RangoHorario(doc.getHoraInicio(), doc.getHoraFin()),
                doc.isReservado()
        );
    }
}
