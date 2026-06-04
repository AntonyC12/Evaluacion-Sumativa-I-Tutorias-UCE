package com.tutorias.presentation.controller;

import com.tutorias.application.service.UsuarioService;
import com.tutorias.infrastructure.notification.NotificationService;
import com.tutorias.domain.model.Usuario;
import com.tutorias.domain.model.Estudiante;
import com.tutorias.domain.model.Docente;
import com.tutorias.domain.model.Notificacion;
import com.tutorias.presentation.dto.request.UserCreationRequest;
import com.tutorias.presentation.dto.response.UsuarioResponse;
import com.tutorias.presentation.mapper.UsuarioMapper;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class UsuarioController {
    private final UsuarioService usuarioService;
    private final NotificationService notificationService;

    public UsuarioController(UsuarioService usuarioService, NotificationService notificationService) {
        this.usuarioService = usuarioService;
        this.notificationService = notificationService;
    }

    @GetMapping("/usuarios")
    public List<UsuarioResponse> listarUsuarios(@RequestParam(value = "rol", required = false) String rol) {
        return usuarioService.listarUsuarios(rol).stream()
                .map(UsuarioMapper::toResponse)
                .collect(Collectors.toList());
    }

    @PostMapping("/usuarios")
    public UsuarioResponse crearUsuario(@RequestBody UserCreationRequest req) {
        Usuario u = usuarioService.crearUsuario(
                req.nombre(),
                req.apellido(),
                req.correoInstitucional(),
                req.contrasena(),
                req.rol(),
                req.estado(),
                req.carrera(),
                req.nivel(),
                req.paralelo(),
                req.departamento()
        );
        return UsuarioMapper.toResponse(u);
    }

    @PutMapping("/usuarios/{id}/toggle")
    public UsuarioResponse toggleUsuario(@PathVariable("id") String id) {
        return UsuarioMapper.toResponse(usuarioService.toggleEstado(id));
    }

    @GetMapping("/estudiantes/usuario/{userId}")
    public Estudiante obtenerEstudiantePorUsuario(@PathVariable("userId") String userId) {
        return usuarioService.obtenerEstudiantePorUsuarioId(userId);
    }

    @GetMapping("/docentes/usuario/{userId}")
    public Docente obtenerDocentePorUsuario(@PathVariable("userId") String userId) {
        return usuarioService.obtenerDocentePorUsuarioId(userId);
    }

    @GetMapping("/notificaciones")
    public List<Notificacion> listarNotificaciones(@RequestParam("destinatario") String destinatario) {
        return notificationService.listarNotificaciones(destinatario);
    }
}
