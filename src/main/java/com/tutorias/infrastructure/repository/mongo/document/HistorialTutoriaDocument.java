package com.tutorias.infrastructure.repository.mongo.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "historial_cambios")
public class HistorialTutoriaDocument {
    @Id
    private String id;
    private String tutoriaId;
    private String estadoAnterior;
    private String estadoNuevo;
    private String descripcion;
    private String fechaEvento; // yyyy-MM-dd HH:mm:ss
    private String usuarioResponsable; // correo del usuario

    public HistorialTutoriaDocument() {}

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTutoriaId() { return tutoriaId; }
    public void setTutoriaId(String tutoriaId) { this.tutoriaId = tutoriaId; }

    public String getEstadoAnterior() { return estadoAnterior; }
    public void setEstadoAnterior(String estadoAnterior) { this.estadoAnterior = estadoAnterior; }

    public String getEstadoNuevo() { return estadoNuevo; }
    public void setEstadoNuevo(String estadoNuevo) { this.estadoNuevo = estadoNuevo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getFechaEvento() { return fechaEvento; }
    public void setFechaEvento(String fechaEvento) { this.fechaEvento = fechaEvento; }

    public String getUsuarioResponsable() { return usuarioResponsable; }
    public void setUsuarioResponsable(String usuarioResponsable) { this.usuarioResponsable = usuarioResponsable; }
}
