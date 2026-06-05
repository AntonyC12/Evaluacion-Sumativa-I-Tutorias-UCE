package ec.edu.uce.tutorias.infrastructure.config;

import ec.edu.uce.tutorias.domain.model.Disponibilidad;
import ec.edu.uce.tutorias.domain.model.Materia;
import ec.edu.uce.tutorias.domain.model.Tutoria;
import ec.edu.uce.tutorias.domain.model.Usuario;
import ec.edu.uce.tutorias.domain.repository.DisponibilidadRepository;
import ec.edu.uce.tutorias.domain.repository.MateriaRepository;
import ec.edu.uce.tutorias.domain.repository.TutoriaRepository;
import ec.edu.uce.tutorias.domain.repository.UsuarioRepository;
import ec.edu.uce.tutorias.domain.vo.CorreoInstitucional;
import ec.edu.uce.tutorias.domain.vo.MotivoTutoria;
import ec.edu.uce.tutorias.domain.vo.RangoHorario;
import ec.edu.uce.tutorias.domain.vo.Rol;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final MateriaRepository materiaRepository;
    private final DisponibilidadRepository disponibilidadRepository;
    private final TutoriaRepository tutoriaRepository;

    public DatabaseSeeder(UsuarioRepository usuarioRepository,
                          MateriaRepository materiaRepository,
                          DisponibilidadRepository disponibilidadRepository,
                          TutoriaRepository tutoriaRepository) {
        this.usuarioRepository = usuarioRepository;
        this.materiaRepository = materiaRepository;
        this.disponibilidadRepository = disponibilidadRepository;
        this.tutoriaRepository = tutoriaRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (usuarioRepository.buscarTodos().isEmpty()) {
            System.out.println("====== INICIALIZANDO DATOS SEMILLA ======");

            // 1. Crear Usuarios
            Usuario estudiante = new Usuario("est1", new CorreoInstitucional("estudiante@uce.edu.ec"), Rol.ESTUDIANTE);
            Usuario docente = new Usuario("doc1", new CorreoInstitucional("docente@uce.edu.ec"), Rol.DOCENTE);
            Usuario admin = new Usuario("admin1", new CorreoInstitucional("admin@uce.edu.ec"), Rol.ADMINISTRADOR);

            usuarioRepository.guardar(estudiante);
            usuarioRepository.guardar(docente);
            usuarioRepository.guardar(admin);

            // 2. Crear Materia
            Materia materia = new Materia("mat1", "Arquitectura de Software");
            materiaRepository.guardar(materia);

            // 3. Crear Disponibilidad Libre
            LocalDate mañana = LocalDate.now().plusDays(1);
            Disponibilidad dispLibre = new Disponibilidad(
                    "disp1",
                    "doc1",
                    "mat1",
                    mañana,
                    new RangoHorario(LocalTime.of(9, 0), LocalTime.of(10, 0))
            );
            disponibilidadRepository.guardar(dispLibre);

            // 4. Crear Disponibilidad Reservada y Tutoria
            Disponibilidad dispReservada = new Disponibilidad(
                    "disp2",
                    "doc1",
                    "mat1",
                    mañana,
                    new RangoHorario(LocalTime.of(10, 0), LocalTime.of(11, 0)),
                    true // reservada
            );
            disponibilidadRepository.guardar(dispReservada);

            Tutoria tutoria = new Tutoria(
                    "tut1",
                    "est1",
                    "doc1",
                    "mat1",
                    "disp2",
                    LocalDateTime.of(mañana, LocalTime.of(10, 0)),
                    LocalDateTime.of(mañana, LocalTime.of(11, 0)),
                    new MotivoTutoria("Dudas sobre patrones de diseño en DDD")
            );
            tutoriaRepository.guardar(tutoria);

            System.out.println("====== DATOS SEMILLA CARGADOS EXITOSAMENTE ======");
        }
    }
}
