package com.tutorias.domain.model;

public class Docente {
    private String id;
    private String usuarioId;
    private String departamento;

    public Docente() {}

    public Docente(String id, String usuarioId, String departamento) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.departamento = departamento;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUsuarioId() { return usuarioId; }
    public void setUsuarioId(String usuarioId) { this.usuarioId = usuarioId; }

    public String getDepartamento() { return departamento; }
    public void setDepartamento(String departamento) { this.departamento = departamento; }
}
