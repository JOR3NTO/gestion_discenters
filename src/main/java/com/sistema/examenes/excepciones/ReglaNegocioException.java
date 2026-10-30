package com.sistema.examenes.excepciones;

/** Se violó una regla de negocio: duplicado, cruce de horarios, cupo lleno, etc. (HTTP 409). */
public class ReglaNegocioException extends RuntimeException {

    public ReglaNegocioException(String mensaje) {
        super(mensaje);
    }
}
