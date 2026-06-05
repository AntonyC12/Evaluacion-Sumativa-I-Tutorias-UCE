package com.tutorias.infrastructure.semillas;

import com.tutorias.domain.entities.Docente;
import com.tutorias.domain.entities.Estudiante;
import com.tutorias.domain.entities.Materia;
import com.tutorias.domain.entities.Usuario;
import com.tutorias.domain.entities.Disponibilidad;
import com.tutorias.domain.enums.EstadoUsuario;
import com.tutorias.domain.enums.RolUsuario;
import com.tutorias.domain.repositorios.DocenteRepository;
import com.tutorias.domain.repositorios.EstudianteRepository;
import com.tutorias.domain.repositorios.MateriaRepository;
import com.tutorias.domain.repositorios.UsuarioRepository;
import com.tutorias.domain.repositorios.DisponibilidadRepository;
import com.tutorias.domain.valueobjects.CorreoInstitucional;
import com.tutorias.domain.valueobjects.RangoHorario;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DatabaseSeeder implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final EstudianteRepository estudianteRepository;
    private final DocenteRepository docenteRepository;
    private final MateriaRepository materiaRepository;
    private final DisponibilidadRepository disponibilidadRepository;

    @Override
    public void run(String... args) throws Exception {
        if (usuarioRepository.findAll().isEmpty()) {
            log.info("Database is empty. Starting database seeding...");

            // 1. Create Users
            String studentPw = BCrypt.hashpw("estudiante123", BCrypt.gensalt());
            Usuario u1 = new Usuario(null, new CorreoInstitucional("estudiante1@uce.edu.ec"), studentPw, RolUsuario.ESTUDIANTE, EstadoUsuario.ACTIVO, "Antony Coello");
            u1 = usuarioRepository.save(u1);

            Usuario u2 = new Usuario(null, new CorreoInstitucional("estudiante2@universidad.edu.ec"), studentPw, RolUsuario.ESTUDIANTE, EstadoUsuario.ACTIVO, "Maria Lopez");
            u2 = usuarioRepository.save(u2);

            String teacherPw = BCrypt.hashpw("docente123", BCrypt.gensalt());
            Usuario u3 = new Usuario(null, new CorreoInstitucional("docente1@uce.edu.ec"), teacherPw, RolUsuario.DOCENTE, EstadoUsuario.ACTIVO, "Dr. Edison Mora");
            u3 = usuarioRepository.save(u3);

            Usuario u4 = new Usuario(null, new CorreoInstitucional("docente2@universidad.edu.ec"), teacherPw, RolUsuario.DOCENTE, EstadoUsuario.ACTIVO, "Dra. Ana Castro");
            u4 = usuarioRepository.save(u4);

            String adminPw = BCrypt.hashpw("admin123", BCrypt.gensalt());
            Usuario u5 = new Usuario(null, new CorreoInstitucional("admin@uce.edu.ec"), adminPw, RolUsuario.ADMINISTRADOR, EstadoUsuario.ACTIVO, "Admin Tutorias");
            u5 = usuarioRepository.save(u5);

            // 2. Create Student Profiles
            Estudiante e1 = new Estudiante(u1.getId(), u1.getId(), u1.getNombre(), "Ingeniería en Computación");
            estudianteRepository.save(e1);

            Estudiante e2 = new Estudiante(u2.getId(), u2.getId(), u2.getNombre(), "Ingeniería Química");
            estudianteRepository.save(e2);

            // 3. Create Teacher Profiles
            Docente d1 = new Docente(u3.getId(), u3.getId(), u3.getNombre(), "Cubiculo 402");
            d1 = docenteRepository.save(d1);

            Docente d2 = new Docente(u4.getId(), u4.getId(), u4.getNombre(), "Cubiculo 105");
            d2 = docenteRepository.save(d2);

            // 4. Create Subjects (Materias)
            Materia m1 = new Materia(null, "Arquitectura de Software", "ARQ-001");
            materiaRepository.save(m1);

            Materia m2 = new Materia(null, "Base de Datos II", "BD2-002");
            materiaRepository.save(m2);

            Materia m3 = new Materia(null, "Programación Web", "WEB-003");
            materiaRepository.save(m3);

            // 5. Seed Availabilities
            LocalDate tomorrow = LocalDate.now().plusDays(1);
            LocalDate dayAfterTomorrow = LocalDate.now().plusDays(2);

            Disponibilidad disp1 = new Disponibilidad(null, d1.getId(), tomorrow, new RangoHorario(LocalTime.of(9, 0), LocalTime.of(10, 0)), false);
            disponibilidadRepository.save(disp1);

            Disponibilidad disp2 = new Disponibilidad(null, d1.getId(), tomorrow, new RangoHorario(LocalTime.of(10, 0), LocalTime.of(11, 0)), false);
            disponibilidadRepository.save(disp2);

            Disponibilidad disp3 = new Disponibilidad(null, d2.getId(), tomorrow, new RangoHorario(LocalTime.of(14, 0), LocalTime.of(15, 0)), false);
            disponibilidadRepository.save(disp3);

            Disponibilidad disp4 = new Disponibilidad(null, d2.getId(), dayAfterTomorrow, new RangoHorario(LocalTime.of(15, 0), LocalTime.of(16, 0)), false);
            disponibilidadRepository.save(disp4);

            log.info("Database seeding completed successfully.");
        } else {
            log.info("Database already contains data. Seeding skipped.");
        }
    }
}
