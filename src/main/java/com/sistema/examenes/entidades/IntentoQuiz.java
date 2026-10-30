package com.sistema.examenes.entidades;

import java.time.LocalDateTime;

import jakarta.persistence.*;

/**
 * Resultado de un estudiante al responder un quiz.
 */
@Entity
@Table(name = "intentos_quiz")
public class IntentoQuiz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Quiz quiz;

    @ManyToOne(optional = false)
    private Estudiante estudiante;

    private int correctas;
    private int total;
    private double puntaje;
    private LocalDateTime fecha = LocalDateTime.now();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Quiz getQuiz() { return quiz; }
    public void setQuiz(Quiz quiz) { this.quiz = quiz; }
    public Estudiante getEstudiante() { return estudiante; }
    public void setEstudiante(Estudiante estudiante) { this.estudiante = estudiante; }
    public int getCorrectas() { return correctas; }
    public void setCorrectas(int correctas) { this.correctas = correctas; }
    public int getTotal() { return total; }
    public void setTotal(int total) { this.total = total; }
    public double getPuntaje() { return puntaje; }
    public void setPuntaje(double puntaje) { this.puntaje = puntaje; }
    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }
}
