package com.sistema.examenes.entidades;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;


@Entity
@Table(name = "roles")
public class Rol {
    @Id
    private Long rolId;
    private String nombre;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "rol")
    private Set<UsuarioRol> usuarioRoles = new HashSet<>();


    
}

