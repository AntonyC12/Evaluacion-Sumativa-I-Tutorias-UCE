package com.tutorias.domain.model;

public class Estudiante {
    private String id;
    private String usuarioId;
    private String carrera;
    private String nivel;
    private String paralelo;

    public Estudiante() {}

    public Estudiante(String id, String usuarioId, String carrera, String nivel, String paralelo) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.carrera = carrera;
        this.nivel = nivel;
        this.paralelo = paralelo;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUsuarioId() { return usuarioId; }
    public void setUsuarioId(String usuarioId) { this.usuarioId = usuarioId; }

    public String getCarrera() { return carrera; }
    public void setCarrera(String carrera) { this.carrera = carrera; }

    public String getNivel() { return nivel; }
    public void setNivel(String nivel) { this.nivel = nivel; }

    public String getParalelo() { return paralelo; }
    public void setParalelo(String paralelo) { this.paralelo = paralelo; }
}
