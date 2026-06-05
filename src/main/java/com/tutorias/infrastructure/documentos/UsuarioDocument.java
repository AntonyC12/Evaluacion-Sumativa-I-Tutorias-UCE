package com.tutorias.infrastructure.documentos;

import com.tutorias.domain.entities.Usuario;
import com.tutorias.domain.enums.EstadoUsuario;
import com.tutorias.domain.enums.RolUsuario;
import com.tutorias.domain.valueobjects.CorreoInstitucional;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "usuarios")
public class UsuarioDocument {
    @Id
    private String id;
    @Indexed(unique = true)
    private String correo;
    private String contrasenia;
    private RolUsuario rol;
    private EstadoUsuario estado;
    private String nombre;

    public static UsuarioDocument fromDomain(Usuario u) {
        if (u == null) return null;
        return new UsuarioDocument(
            u.getId(),
            u.getCorreo() != null ? u.getCorreo().valor() : null,
            u.getContrasenia(),
            u.getRol(),
            u.getEstado(),
            u.getNombre()
        );
    }

    public Usuario toDomain() {
        return new Usuario(
            this.id,
            this.correo != null ? new CorreoInstitucional(this.correo) : null,
            this.contrasenia,
            this.rol,
            this.estado,
            this.nombre
        );
    }
}
