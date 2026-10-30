package com.sistema.examenes.dto;

import jakarta.validation.constraints.NotNull;

public record AsignacionRequest(@NotNull Long salonId, @NotNull Long horarioId) {
}
