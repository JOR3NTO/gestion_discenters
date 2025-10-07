package com.sistema.examenes.entidades;

import jakarta.persistence.*;

@Entity
@Table(name = "salones")
public class Salon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private int capacidad;
    // getters y setters
}
