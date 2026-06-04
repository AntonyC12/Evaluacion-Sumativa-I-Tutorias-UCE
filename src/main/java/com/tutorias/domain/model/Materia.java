package com.tutorias.domain.model;

public class Materia {
    private String id;
    private String codigo;
    private String nombre;
    private String estado; // Activo, Inactivo

    public Materia() {}

    public Materia(String id, String codigo, String nombre, String estado) {
        this.id = id;
        this.codigo = codigo;
        this.nombre = nombre;
        this.estado = estado;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
