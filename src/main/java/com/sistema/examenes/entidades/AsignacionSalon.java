package com.sistema.examenes.entidades;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;

/**
 * Asigna un salón a una franja horaria y guarda los estudiantes inscritos,
 * que no pueden superar la capacidad del salón.
 */
@Entity
@Table(name = "asignaciones_salon",
        uniqueConstraints = @UniqueConstraint(columnNames = { "salon_id", "horario_id" }))
public class AsignacionSalon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Salon salon;

    @ManyToOne(optional = false)
    private HorarioClase horario;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "asignacion_estudiantes",
            joinColumns = @JoinColumn(name = "asignacion_id"),
            inverseJoinColumns = @JoinColumn(name = "estudiante_id"))
    private Set<Estudiante> estudiantes = new HashSet<>();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Salon getSalon() { return salon; }
    public void setSalon(Salon salon) { this.salon = salon; }
    public HorarioClase getHorario() { return horario; }
    public void setHorario(HorarioClase horario) { this.horario = horario; }
    public Set<Estudiante> getEstudiantes() { return estudiantes; }
    public void setEstudiantes(Set<Estudiante> estudiantes) { this.estudiantes = estudiantes; }
}
