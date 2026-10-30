package com.sistema.examenes.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sistema.examenes.entidades.Estudiante;

public interface EstudianteRepository extends JpaRepository<Estudiante, Long> {
    boolean existsByMatricula(String matricula);
    boolean existsByEmail(String email);
}
