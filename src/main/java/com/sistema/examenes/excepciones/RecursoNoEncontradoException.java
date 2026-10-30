package com.sistema.examenes.excepciones;

/** El recurso solicitado no existe (HTTP 404). */
public class RecursoNoEncontradoException extends RuntimeException {

    public RecursoNoEncontradoException(String recurso, Long id) {
        super(recurso + " con id " + id + " no encontrado");
    }
}
