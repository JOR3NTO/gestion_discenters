package com.sistema.examenes.servicios;

import com.sistema.examenes.entidades.Estudiante;
import java.util.List;

public interface EstudianteService {
    Estudiante registrarEstudiante(Estudiante estudiante);
    List<Estudiante> listarEstudiantes();
    Estudiante obtenerEstudiantePorId(Long id);
    void eliminarEstudiante(Long id);
}
