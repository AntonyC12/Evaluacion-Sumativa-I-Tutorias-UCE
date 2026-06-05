package com.tutorias.presentation.controladores;

import com.tutorias.application.servicios.UsuarioApplicationService;
import com.tutorias.domain.entities.Usuario;
import com.tutorias.presentation.dto.LoginRequest;
import com.tutorias.presentation.dto.LoginResponse;
import com.tutorias.presentation.dto.UsuarioResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Autenticación", description = "Endpoints para iniciar sesión y consulta básica de cuentas")
public class AutenticacionController {

    private final UsuarioApplicationService usuarioApplicationService;

    @PostMapping("/login")
    @Operation(summary = "Iniciar sesión con correo institucional y contraseña")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        Usuario usuario = usuarioApplicationService.autenticar(request.getCorreo(), request.getContrasenia());
        return new LoginResponse(
                usuario.getId(),
                usuario.getCorreo().valor(),
                usuario.getRol(),
                usuario.getNombre()
        );
    }

    @GetMapping("/usuarios")
    @Operation(summary = "Listar todas las cuentas de usuario (útil para pruebas y simulación)")
    public List<UsuarioResponse> listarUsuarios() {
        return usuarioApplicationService.listarTodos().stream()
                .map(u -> new UsuarioResponse(
                        u.getId(),
                        u.getCorreo().valor(),
                        u.getRol(),
                        u.getEstado(),
                        u.getNombre()
                ))
                .collect(Collectors.toList());
    }
}
