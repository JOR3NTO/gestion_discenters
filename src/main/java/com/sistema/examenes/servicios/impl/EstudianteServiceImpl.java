package com.sistema.examenes.servicios.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sistema.examenes.entidades.Estudiante;
import com.sistema.examenes.excepciones.RecursoNoEncontradoException;
import com.sistema.examenes.excepciones.ReglaNegocioException;
import com.sistema.examenes.repositorios.EstudianteRepository;
import com.sistema.examenes.servicios.EstudianteService;

@Service
public class EstudianteServiceImpl implements EstudianteService {

    private final EstudianteRepository estudianteRepository;

    public EstudianteServiceImpl(EstudianteRepository estudianteRepository) {
        this.estudianteRepository = estudianteRepository;
    }

    @Override
    public Estudiante registrarEstudiante(Estudiante estudiante) {
        if (estudianteRepository.existsByMatricula(estudiante.getMatricula())) {
            throw new ReglaNegocioException("Ya existe un estudiante con la matrícula " + estudiante.getMatricula());
        }
        if (estudianteRepository.existsByEmail(estudiante.getEmail())) {
            throw new ReglaNegocioException("Ya existe un estudiante con el email " + estudiante.getEmail());
        }
        estudiante.setId(null);
        return estudianteRepository.save(estudiante);
    }

    @Override
    public List<Estudiante> listarEstudiantes() {
        return estudianteRepository.findAll();
    }

    @Override
    public Estudiante obtenerEstudiantePorId(Long id) {
        return estudianteRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Estudiante", id));
    }

    @Override
    public Estudiante actualizarEstudiante(Long id, Estudiante datos) {
        Estudiante actual = obtenerEstudiantePorId(id);
        if (!actual.getMatricula().equals(datos.getMatricula())
                && estudianteRepository.existsByMatricula(datos.getMatricula())) {
            throw new ReglaNegocioException("Ya existe un estudiante con la matrícula " + datos.getMatricula());
        }
        if (!actual.getEmail().equals(datos.getEmail()) && estudianteRepository.existsByEmail(datos.getEmail())) {
            throw new ReglaNegocioException("Ya existe un estudiante con el email " + datos.getEmail());
        }
        actual.setNombre(datos.getNombre());
        actual.setApellido(datos.getApellido());
        actual.setEmail(datos.getEmail());
        actual.setMatricula(datos.getMatricula());
        return estudianteRepository.save(actual);
    }

    @Override
    public void eliminarEstudiante(Long id) {
        estudianteRepository.delete(obtenerEstudiantePorId(id));
    }
}
