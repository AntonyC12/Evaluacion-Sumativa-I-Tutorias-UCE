package com.tutorias.presentation.controller;

import com.tutorias.application.service.AuthService;
import com.tutorias.presentation.dto.request.LoginRequest;
import com.tutorias.presentation.dto.response.UsuarioResponse;
import com.tutorias.presentation.mapper.UsuarioMapper;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public UsuarioResponse login(@RequestBody LoginRequest req) {
        return UsuarioMapper.toResponse(authService.login(req.correoInstitucional(), req.contrasena()));
    }
}
