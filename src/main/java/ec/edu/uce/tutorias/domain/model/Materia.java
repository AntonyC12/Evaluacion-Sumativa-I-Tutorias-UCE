package ec.edu.uce.tutorias.domain.model;

import ec.edu.uce.tutorias.domain.exception.DomainException;

public class Materia {
    
    private final String id;
    private final String nombre;

    public Materia(String id, String nombre) {
        if (id == null || id.trim().isEmpty()) {
            throw new DomainException("El ID de la materia es obligatorio");
        }
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new DomainException("El nombre de la materia es obligatorio");
        }
        this.id = id;
        this.nombre = nombre;
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }
}
