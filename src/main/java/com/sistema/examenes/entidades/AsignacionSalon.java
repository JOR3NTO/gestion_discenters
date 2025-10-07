package com.sistema.examenes.entidades;

import jakarta.persistence.*;

@Entity
@Table(name = "asignaciones_salon")
public class AsignacionSalon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Salon salon;
    @ManyToOne
    private HorarioClase horario;
    // getters y setters
}
