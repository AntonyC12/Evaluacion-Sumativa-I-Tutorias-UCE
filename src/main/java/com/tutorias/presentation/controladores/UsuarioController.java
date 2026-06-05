package com.tutorias.presentation.controladores;

import com.tutorias.application.servicios.UsuarioApplicationService;
import com.tutorias.presentation.dto.EstadoUsuarioRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Usuarios", description = "Administración de usuarios (Activación/Desactivación)")
public class UsuarioController {

    private final UsuarioApplicationService usuarioApplicationService;

    @PutMapping("/{id}/estado")
    @Operation(summary = "Activar o desactivar cuenta de usuario (requiere rol administrador)")
    public void cambiarEstado(
            @PathVariable String id,
            @RequestBody EstadoUsuarioRequest request,
            @RequestHeader("X-Admin-Id") String adminUserId) {
        usuarioApplicationService.cambiarEstado(adminUserId, id, request.isActivo());
    }
}
