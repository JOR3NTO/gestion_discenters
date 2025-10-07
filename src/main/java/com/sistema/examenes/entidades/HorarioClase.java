package com.sistema.examenes.entidades;

import jakarta.persistence.*;
import java.time.LocalTime;

@Entity
@Table(name = "horarios_clase")
public class HorarioClase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String diaSemana;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    // getters y setters
}
