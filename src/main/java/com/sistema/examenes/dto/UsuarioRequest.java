package com.sistema.examenes.dto;

import java.util.Set;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/** Datos para crear un usuario; {@code rolIds} son los ids de roles existentes. */
public record UsuarioRequest(
        @NotBlank String username,
        @NotBlank @Size(min = 6) String password,
        @NotBlank String nombre,
        String apellido,
        @NotBlank @Email String email,
        String telefono,
        String perfil,
        Set<Long> rolIds) {
}
