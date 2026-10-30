package com.sistema.examenes.entidades;

import java.time.LocalTime;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

/**
 * Franja horaria de una clase: día de la semana y rango de horas (formato HH:mm).
 */
@Entity
@Table(name = "horarios_clase")
public class HorarioClase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    private DiaSemana diaSemana;

    @NotNull
    private LocalTime horaInicio;

    @NotNull
    private LocalTime horaFin;

    /** Indica si este horario se cruza con otro en el mismo día. */
    public boolean seCruzaCon(HorarioClase otro) {
        return diaSemana == otro.diaSemana
                && horaInicio.isBefore(otro.horaFin)
                && otro.horaInicio.isBefore(horaFin);
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public DiaSemana getDiaSemana() { return diaSemana; }
    public void setDiaSemana(DiaSemana diaSemana) { this.diaSemana = diaSemana; }
    public LocalTime getHoraInicio() { return horaInicio; }
    public void setHoraInicio(LocalTime horaInicio) { this.horaInicio = horaInicio; }
    public LocalTime getHoraFin() { return horaFin; }
    public void setHoraFin(LocalTime horaFin) { this.horaFin = horaFin; }
}
