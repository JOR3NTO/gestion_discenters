package com.sistema.examenes.entidades;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;

/**
 * Entidad que representa la relación entre un usuario y un rol dentro del sistema.
 * Cada instancia de esta clase asocia un usuario con un rol específico.
 *
 * Campos:
 * - usuarioRolId: Identificador único de la relación usuario-rol.
 * - usuario: Referencia al usuario asociado (se omite en el JSON para evitar ciclos).
 * - rol: Referencia al rol asignado al usuario.
 */
@Entity
@Table(name = "usuario_roles")
public class UsuarioRol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long usuarioRolId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    private Usuario usuario;

    @ManyToOne
    private Rol rol;

    public Long getUsuarioRolId() { return usuarioRolId; }
    public void setUsuarioRolId(Long usuarioRolId) { this.usuarioRolId = usuarioRolId; }
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    public Rol getRol() { return rol; }
    public void setRol(Rol rol) { this.rol = rol; }
}
