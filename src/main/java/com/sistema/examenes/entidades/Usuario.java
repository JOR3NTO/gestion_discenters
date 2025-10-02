package com.sistema.examenes.entidades;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;

/**
 * Representa un usuario dentro del sistema de exámenes.
 * 
 * Esta entidad almacena la información básica de un usuario, incluyendo su nombre,
 * apellido, correo electrónico, teléfono, nombre de usuario, contraseña, estado de habilitación
 * y perfil. Además, mantiene una relación con los roles asignados al usuario.
 * 
 * Campos:
 * - id: Identificador único del usuario (clave primaria, autogenerado).
 * - username: Nombre de usuario para autenticación.
 * - password: Contraseña del usuario.
 * - nombre: Nombre real del usuario.
 * - apellido: Apellido del usuario.
 * - email: Correo electrónico del usuario.
 * - telefono: Número de teléfono del usuario.
 * - enabled: Indica si el usuario está habilitado o no.
 * - perfil: Ruta o nombre de la imagen de perfil del usuario.
 * - usuarioRoles: Conjunto de roles asociados al usuario.
 * 
 * Relación @OneToMany:
 * La anotación {@code @OneToMany} en el campo {@code usuarioRoles} indica que un usuario puede tener
 * múltiples roles asociados (relación uno a muchos). El atributo {@code mappedBy = "usuario"} especifica
 * que la relación está mapeada por el campo "usuario" en la entidad {@code UsuarioRol}. 
 * El {@code cascade = CascadeType.ALL} permite que las operaciones realizadas sobre el usuario (guardar, eliminar, etc.)
 * se propaguen a los roles asociados. El {@code fetch = FetchType.EAGER} indica que los roles se cargarán 
 * automáticamente junto con el usuario.
 */
@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String username;;
    private String password;
    private String nombre;
    private String apellido;
    private String email;
    private String telefono; 
    private boolean enabled = true;
    private String perfil;
    
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "usuario")
    private Set<UsuarioRol> usuarioRoles = new HashSet<>();
}
