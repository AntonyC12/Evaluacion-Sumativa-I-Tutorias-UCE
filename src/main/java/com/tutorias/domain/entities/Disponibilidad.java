package com.tutorias.domain.entities;

import com.tutorias.domain.exceptions.DomainException;
import com.tutorias.domain.valueobjects.RangoHorario;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Disponibilidad {
    private String id;
    private String docenteId;
    private LocalDate fecha;
    private RangoHorario rangoHorario;
    private boolean reservada;

    public void reservar() {
        if (reservada) {
            throw new DomainException("La disponibilidad ya se encuentra reservada.");
        }
        this.reservada = true;
    }

    public void liberar() {
        this.reservada = false;
    }

    public void validarParaEliminacion() {
        if (reservada) {
            throw new DomainException("No se puede eliminar una disponibilidad que ya está reservada.");
        }
    }

    public void validarParaEdicion() {
        if (reservada) {
            throw new DomainException("No se puede editar una disponibilidad que ya está reservada.");
        }
    }
}
