package ec.edu.uce.tutorias.domain.vo;

import ec.edu.uce.tutorias.domain.exception.DomainException;

import java.util.Objects;
import java.util.regex.Pattern;

public class CorreoInstitucional {

    private static final String DOMINIO_PERMITIDO = "@uce.edu.ec";
    private static final Pattern PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@uce\\.edu\\.ec$");

    private final String valor;

    public CorreoInstitucional(String valor) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new DomainException("El correo institucional no puede ser nulo o vacío");
        }
        if (!PATTERN.matcher(valor).matches()) {
            throw new DomainException("El correo debe tener formato institucional " + DOMINIO_PERMITIDO);
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
        CorreoInstitucional that = (CorreoInstitucional) o;
        return Objects.equals(valor, that.valor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(valor);
    }
}
