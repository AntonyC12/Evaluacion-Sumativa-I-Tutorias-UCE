package com.tutorias.infrastructure.config;

import com.tutorias.domain.model.*;
import com.tutorias.domain.valueobject.CorreoInstitucional;
import com.tutorias.domain.enums.EstadoUsuario;
import com.tutorias.domain.enums.RolUsuario;
import com.tutorias.infrastructure.repository.UsuarioRepository;
import com.tutorias.infrastructure.repository.MateriaRepository;
import com.tutorias.infrastructure.repository.DisponibilidadRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

@Component
public class DataSeeder implements CommandLineRunner {
    private final UsuarioRepository usuarioRepo;
    private final MateriaRepository materiaRepo;
    private final DisponibilidadRepository disponibilidadRepo;

    public DataSeeder(UsuarioRepository usuarioRepo,
                      MateriaRepository materiaRepo,
                      DisponibilidadRepository disponibilidadRepo) {
        this.usuarioRepo = usuarioRepo;
        this.materiaRepo = materiaRepo;
        this.disponibilidadRepo = disponibilidadRepo;
    }

    @Override
    public void run(String... args) throws Exception {
        if (usuarioRepo.count() == 0) {
            System.out.println("Base de datos vacia. Cargando datos semilla de tutorias...");

            // 1. Materias
            Materia m1 = new Materia("1", "AS-001", "Arquitectura de Software", "Activo");
            Materia m2 = new Materia("2", "BD-002", "Base de Datos", "Activo");
            Materia m3 = new Materia("3", "RC-003", "Redes de Computadoras", "Activo");
            materiaRepo.save(m1);
            materiaRepo.save(m2);
            materiaRepo.save(m3);

            // 2. Usuarios (con enums y value objects tipados)
            LocalDateTime baseTime = LocalDateTime.of(2026, 6, 3, 12, 0, 0);

            Usuario uAdmin = new Usuario("u1", "Admin", "General",
                    new CorreoInstitucional("admin@uce.edu.ec"), "admin123",
                    RolUsuario.ADMINISTRADOR, EstadoUsuario.ACTIVO, baseTime);
            Usuario uDoc1 = new Usuario("u2", "Carlos", "Perez",
                    new CorreoInstitucional("docente1@uce.edu.ec"), "docente123",
                    RolUsuario.DOCENTE, EstadoUsuario.ACTIVO, baseTime);
            Usuario uDoc2 = new Usuario("u3", "Maria", "Flores",
                    new CorreoInstitucional("docente2@uce.edu.ec"), "docente123",
                    RolUsuario.DOCENTE, EstadoUsuario.ACTIVO, baseTime);
            Usuario uEst1 = new Usuario("u4", "Antony", "Coello",
                    new CorreoInstitucional("estudiante1@uce.edu.ec"), "estudiante123",
                    RolUsuario.ESTUDIANTE, EstadoUsuario.ACTIVO, baseTime);
            Usuario uEst2 = new Usuario("u5", "Juan", "Lopez",
                    new CorreoInstitucional("estudiante2@uce.edu.ec"), "estudiante123",
                    RolUsuario.ESTUDIANTE, EstadoUsuario.ACTIVO, baseTime);
            Usuario uEstInac = new Usuario("u6", "Inactivo", "Prueba",
                    new CorreoInstitucional("inactivo@uce.edu.ec"), "inactivo123",
                    RolUsuario.ESTUDIANTE, EstadoUsuario.INACTIVO, baseTime);

            usuarioRepo.save(uAdmin);
            usuarioRepo.save(uDoc1);
            usuarioRepo.save(uDoc2);
            usuarioRepo.save(uEst1);
            usuarioRepo.save(uEst2);
            usuarioRepo.save(uEstInac);

            // 3. Perfiles de Estudiante (consolidados en UsuarioRepository)
            usuarioRepo.saveEstudiante(new Estudiante("e1", "u4", "Computacion", "7", "A"));
            usuarioRepo.saveEstudiante(new Estudiante("e2", "u5", "Computacion", "7", "B"));
            usuarioRepo.saveEstudiante(new Estudiante("e3", "u6", "Computacion", "1", "A"));

            // 4. Perfiles de Docente (consolidados en UsuarioRepository)
            usuarioRepo.saveDocente(new Docente("d1", "u2", "Computacion"));
            usuarioRepo.saveDocente(new Docente("d2", "u3", "Redes"));

            // 5. Disponibilidades futuras para pruebas
            disponibilidadRepo.save(new Disponibilidad("disp1", "d1", "Carlos Perez", "1",
                    "Arquitectura de Software", LocalDate.of(2026, 7, 10),
                    LocalTime.of(9, 0), LocalTime.of(10, 0), "Disponible"));
            disponibilidadRepo.save(new Disponibilidad("disp2", "d1", "Carlos Perez", "2",
                    "Base de Datos", LocalDate.of(2026, 7, 11),
                    LocalTime.of(11, 0), LocalTime.of(12, 0), "Disponible"));
            disponibilidadRepo.save(new Disponibilidad("disp3", "d2", "Maria Flores", "3",
                    "Redes de Computadoras", LocalDate.of(2026, 7, 12),
                    LocalTime.of(14, 0), LocalTime.of(15, 0), "Disponible"));

            System.out.println("Datos semilla cargados con exito.");
        }
    }
}
