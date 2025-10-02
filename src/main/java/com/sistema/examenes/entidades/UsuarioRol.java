package com.sistema.examenes.entidades;

import jakarta.persistence.*;

/**
 * Entidad que representa la relación entre un usuario y un rol dentro del sistema.
 * Cada instancia de esta clase asocia un usuario con un rol específico.
 * 
 * Campos:
 * - usuarioRolId: Identificador único de la relación usuario-rol.
 * - usuario: Referencia al usuario asociado.
 * - rol: Referencia al rol asignado al usuario.
 * 
 * Esta entidad permite gestionar los roles asignados a los usuarios,
 * facilitando la implementación de control de acceso y permisos.
 */
@Entity
public class UsuarioRol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long usuarioRolId;

    @ManyToOne(fetch = FetchType.EAGER)
    private Usuario usuario;

    @ManyToOne
    private Rol rol;

}
