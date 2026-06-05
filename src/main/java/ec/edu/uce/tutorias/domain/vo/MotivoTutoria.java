package ec.edu.uce.tutorias.domain.vo;

import ec.edu.uce.tutorias.domain.exception.DomainException;
import java.util.Objects;

public class MotivoTutoria {

    private final String valor;

    public MotivoTutoria(String valor) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new DomainException("El motivo de la tutoría no puede estar vacío");
        }
        if (valor.length() > 500) {
            throw new DomainException("El motivo no puede exceder los 500 caracteres");
        }
        this.valor = valor;
    }

    public String getValor() {
        return valor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MotivoTutoria that = (MotivoTutoria) o;
        return Objects.equals(valor, that.valor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(valor);
    }
}
