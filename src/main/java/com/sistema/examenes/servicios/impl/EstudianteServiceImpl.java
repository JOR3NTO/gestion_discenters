package com.sistema.examenes.servicios.impl;

import com.sistema.examenes.entidades.Estudiante;
import com.sistema.examenes.repositorios.EstudianteRepository;
import com.sistema.examenes.servicios.EstudianteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EstudianteServiceImpl implements EstudianteService {
    @Autowired
    private EstudianteRepository estudianteRepository;

    @Override
    public Estudiante registrarEstudiante(Estudiante estudiante) {
        return estudianteRepository.save(estudiante);
    }

    @Override
    public List<Estudiante> listarEstudiantes() {
        return estudianteRepository.findAll();
    }

    @Override
    public Estudiante obtenerEstudiantePorId(Long id) {
        Optional<Estudiante> estudiante = estudianteRepository.findById(id);
        return estudiante.orElse(null);
    }

    @Override
    public void eliminarEstudiante(Long id) {
        estudianteRepository.deleteById(id);
    }
}
