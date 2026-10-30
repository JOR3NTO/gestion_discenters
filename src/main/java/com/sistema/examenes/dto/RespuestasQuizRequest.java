package com.sistema.examenes.dto;

import java.util.Map;

import jakarta.validation.constraints.NotNull;

/** Respuestas de un estudiante: mapa {@code preguntaId -> opcionId}. */
public record RespuestasQuizRequest(@NotNull Long estudianteId, @NotNull Map<Long, Long> respuestas) {
}
