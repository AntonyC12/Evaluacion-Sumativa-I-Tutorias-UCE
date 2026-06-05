package com.tutorias.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Docente {
    private String id;
    private String usuarioId;
    private String nombre;
    private String cubiculo;
}
