package com.sistema.examenes.servicios;

import java.util.List;

import com.sistema.examenes.entidades.Estudiante;

public interface EstudianteService {
    Estudiante registrarEstudiante(Estudiante estudiante);
    List<Estudiante> listarEstudiantes();
    Estudiante obtenerEstudiantePorId(Long id);
    Estudiante actualizarEstudiante(Long id, Estudiante estudiante);
    void eliminarEstudiante(Long id);
}
