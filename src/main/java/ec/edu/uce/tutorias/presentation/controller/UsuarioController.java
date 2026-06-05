package ec.edu.uce.tutorias.presentation.controller;

import ec.edu.uce.tutorias.application.dto.UsuarioResponse;
import ec.edu.uce.tutorias.application.usecase.ListarUsuariosUseCase;
import ec.edu.uce.tutorias.application.usecase.RegistrarUsuarioUseCase;
import ec.edu.uce.tutorias.domain.vo.Rol;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final ListarUsuariosUseCase listarUsuariosUseCase;
    private final RegistrarUsuarioUseCase registrarUsuarioUseCase;

    public UsuarioController(ListarUsuariosUseCase listarUsuariosUseCase,
                             RegistrarUsuarioUseCase registrarUsuarioUseCase) {
        this.listarUsuariosUseCase = listarUsuariosUseCase;
        this.registrarUsuarioUseCase = registrarUsuarioUseCase;
    }

    @GetMapping
    public ResponseEntity<List<UsuarioResponse>> listarUsuarios() {
        List<UsuarioResponse> responses = listarUsuariosUseCase.ejecutar().stream()
                .map(u -> new UsuarioResponse(
                        u.getId(),
                        u.getCorreo().getValor(),
                        u.getRol().name(),
                        u.getEstado().name()
                ))
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @PostMapping
    public ResponseEntity<String> registrarUsuario(@RequestParam String correo, @RequestParam Rol rol) {
        String id = registrarUsuarioUseCase.ejecutar(null, correo, rol);
        return ResponseEntity.ok(id);
    }
}
